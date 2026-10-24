# Face Check-in Module — Smart Park Meeting Attendance

Face recognition check-in module integrated into the smart-park platform.

## Architecture

- K210 board (XiUOS) captures face image via camera
- PC bridge receives photo via TCP (from `pc_bridge/api_bridge.py`)
- Backend (`park-meeting`) processes face verification and attendance logic
- Frontend (Vue) manages meetings, persons, and attendance records

## Docker Setup

```bash
cd docker
docker compose up --build
```

Services started:
| Service | Port | Description |
|---------|------|-------------|
| MySQL | 3307 | Database |
| Redis | 6380 | Cache |
| Nacos | 8850 | Service registry & config |
| Gateway | 8099 | API gateway |
| Park System | 30010 | Auth & system service |
| Park Meeting | 30011 | Meeting & face check-in service |
| Face Inference | 8901 | YuNet/SFace matching service |
| Frontend | 9001 | Vue admin panel |

## Serial/Network Bridge

Board sends photos via TCP to the bridge, which forwards to the backend API:

```bash
python pc_bridge/api_bridge.py --listen-port 9000 --api-base http://localhost:8099 --device-id k210-001 --device-token board-token
```

## Frontend Development

```bash
cd face-checkin
pnpm install
pnpm dev
```

Navigate to Meeting Management > Face Check-in

## Backend Development

```bash
cd smart-park-backend
mvn -s settings.xml package -pl park-meeting -am -DskipTests
```

## API Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /smartPark/park-meeting/face-checkin/verify | Verify face and check in |
| POST | /smartPark/park-meeting/face-checkin/person/save | Register person |
| GET | /smartPark/park-meeting/face-checkin/person/list | List persons |
| DELETE | /smartPark/park-meeting/face-checkin/person/remove/{id} | Remove person |
| POST | /smartPark/park-meeting/face-checkin/member/save | Update meeting members |
| GET | /smartPark/park-meeting/face-checkin/attendance/list | Query attendance |
