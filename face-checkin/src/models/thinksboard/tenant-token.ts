import { ThingsBoardLoginResponse } from './type';

const TENANT_TOKEN_STORAGE_KEY = 'thingsboardTenantTokenCache';

export interface ThingsBoardTenantTokenCache {
  tenant:string
  token:string
  expTime:number
}

type ThingsBoardTenantTokenStore = Record<string, ThingsBoardTenantTokenCache>;

type ThingsBoardTenantTokenClient = {
  getTenantToken:(tenant:string) => Promise<ThingsBoardLoginResponse>
};

const tenantTokenTasks = new Map<string, Promise<ThingsBoardTenantTokenCache>>();

function normalizeTenant(tenant:string) {
  return tenant.trim();
}

function isObject(value:unknown): value is Record<string, unknown> {
  return typeof value === 'object' && value !== null;
}

function normalizeExpTime(rawExpTime:unknown) {
  const expTime = Number(rawExpTime || 0);
  if (!Number.isFinite(expTime) || expTime <= 0) return 0;

  // 兼容秒级时间戳
  if (expTime < 1e12) {
    return expTime * 1000;
  }

  return expTime;
}

function normalizeCache(raw:unknown): ThingsBoardTenantTokenCache | null {
  if (!isObject(raw)) return null;

  const tenant = typeof raw.tenant === 'string' ? raw.tenant.trim() : '';
  const token = typeof raw.token === 'string' ? raw.token.trim() : '';
  const expTime = normalizeExpTime(raw.expTime);

  if (!tenant || !token || !Number.isFinite(expTime) || expTime <= 0) {
    return null;
  }

  return {
    tenant,
    token,
    expTime,
  };
}

function getStore():ThingsBoardTenantTokenStore {
  if (typeof window === 'undefined') return {};

  const rawStore = window.$localStorage[TENANT_TOKEN_STORAGE_KEY];
  if (!isObject(rawStore)) return {};

  return Object.entries(rawStore).reduce<ThingsBoardTenantTokenStore>((store, [tenant, cache]) => {
    const normalized = normalizeCache(cache);
    if (normalized) {
      store[tenant] = normalized;
    }
    return store;
  }, {});
}

function setStore(store:ThingsBoardTenantTokenStore) {
  if (typeof window === 'undefined') return;
  window.$localStorage[TENANT_TOKEN_STORAGE_KEY] = store;
}

function persistCache(cache:ThingsBoardTenantTokenCache) {
  const store = getStore();
  store[cache.tenant] = cache;
  setStore(store);
}

export function clearThingsBoardTenantTokenCache(tenant:string) {
  const normalizedTenant = normalizeTenant(tenant);
  if (!normalizedTenant) return;

  const store = getStore();
  if (!store[normalizedTenant]) return;

  delete store[normalizedTenant];
  setStore(store);
}

export function getThingsBoardTenantTokenCache(tenant:string) {
  const normalizedTenant = normalizeTenant(tenant);
  if (!normalizedTenant) return null;

  const store = getStore();
  return store[normalizedTenant] || null;
}

export function isThingsBoardTenantTokenUsable(
  cache:ThingsBoardTenantTokenCache | null | undefined,
  leadMs = 0,
) {
  if (!cache?.token || !cache.expTime) return false;
  return cache.expTime > Date.now() + Math.max(0, leadMs);
}

export async function ensureThingsBoardTenantToken(
  client:ThingsBoardTenantTokenClient,
  tenant:string,
  options:{
    forceRefresh?:boolean
    refreshLeadMs?:number
  } = {},
) {
  const normalizedTenant = normalizeTenant(tenant);
  if (!normalizedTenant) {
    throw new Error('ThingsBoard tenant 不能为空');
  }

  const refreshLeadMs = Math.max(0, options.refreshLeadMs || 0);
  const cached = getThingsBoardTenantTokenCache(normalizedTenant);

  if (!options.forceRefresh && isThingsBoardTenantTokenUsable(cached, refreshLeadMs)) {
    return cached!;
  }

  const pendingTask = tenantTokenTasks.get(normalizedTenant);
  if (pendingTask) {
    return pendingTask;
  }

  const task: any = client.getTenantToken(normalizedTenant)
    .then((result) => {
      const token = result.token?.trim();
      const expTime = normalizeExpTime(result.expTime);

      if (!token) {
        throw new Error('ThingsBoard tenant token 接口未返回 token');
      }
      if (!Number.isFinite(expTime) || expTime <= 0) {
        throw new Error('ThingsBoard tenant token 接口未返回有效 expTime');
      }

      const nextCache:ThingsBoardTenantTokenCache = {
        tenant: normalizedTenant,
        token,
        expTime,
      };
      persistCache(nextCache);
      return nextCache;
    })
    .catch((error) => {
      if (!options.forceRefresh && isThingsBoardTenantTokenUsable(cached, refreshLeadMs)) {
        return cached;
      }
      throw error;
    })
    .finally(() => {
      tenantTokenTasks.delete(normalizedTenant);
    });

  tenantTokenTasks.set(normalizedTenant, task);
  return task;
}
