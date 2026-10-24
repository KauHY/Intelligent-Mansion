FROM python:3.12-slim
WORKDIR /app
ENV PIP_NO_CACHE_DIR=1
COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt
COPY matcher.py service.py __init__.py ./
COPY models ./models
EXPOSE 8901
CMD ["python", "service.py"]
