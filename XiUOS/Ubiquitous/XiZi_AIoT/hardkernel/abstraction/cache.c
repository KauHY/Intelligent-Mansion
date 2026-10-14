/*
* Copyright (c) 2020 AIIT XUOS Lab
* XiUOS is licensed under Mulan PSL v2.
* You can use this software according to the terms and conditions of the Mulan PSL v2.
* You may obtain a copy of Mulan PSL v2 at:
*        http://license.coscl.org.cn/MulanPSL2
* THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
* EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
* MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
* See the Mulan PSL v2 for more details.
*/

/**
* @file:    cache.c
* @brief:   the general management of system cache
* @version: 3.0
* @author:  AIIT XUOS Lab
* @date:    2023/4/27
*
*/

void InvalidInsCache()
{
    PlatInvalidInsCache();
}

void InvalidDataCache(unsigned long start, unsigned long end)
{
    PlatInvalidDateCache(start, end);
}

void CleanDataCache(unsigned long start, unsigned long end)
{
    PlatCleanDateCache(start, end);
}