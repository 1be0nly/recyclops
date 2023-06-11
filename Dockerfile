# Base image
FROM node:16

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the source code
COPY main_api ./main_api

# Start the application
CMD ["node", "./main_api/server.js"]

# FROM python:3.9-slim

# WORKDIR /app

# COPY requirements.txt .

# RUN pip install --no-cache-dir -r requirements.txt

# COPY ml_handler.py .
# COPY recyclemodel.h5 .

# EXPOSE 8000

# CMD ["python", "ml_handler.py"]