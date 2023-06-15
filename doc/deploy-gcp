# Deploy Server to GCP
---

This is the step to manually deploy the server to Google Cloud Platform. Here are some of the Google Cloud Platform services used :
- Cloud Run
- Cloud Storage
- Firebase
- Container Registry

##### NOTE : Please follow these tutorial step by step!

### The Architecture
---
![](https://github.com/1be0nly/recyclops/blob/main/doc/recyclops-architecture.png?raw=true)

## Google Cloud Storage
---
Create a bucket using Cloud Shell Terminal :
    ```
    gsutil mb -c standard -l asia-southeast1 gs://recylops-bucket
    ```
    __Highly RECOMMENDED bucket name:__ ***recyclops-bucket***

## Environment Variables
---
1. Clone recyclops project from github :
    ```
    git clone https://github.com/1be0nly/recyclops.git
    
    cd recyclops/'Cloud Computing'/
    ```
2. Create file .env in ***Cloud Computing*** folder :
    ```
    nano .env
    ```
3. Copy this to your .env file :
    ```
    BUCKET_NAME = '<YOUR BUCKET NAME>' #Updated Later
    FLASK_API_URL = '<YOUR CLOUD RUN ML API URL>' #Updated Later
    DATABASE_URL = '<YOUR REALTIME DATABSAE FIREBASE>' #Updated Later
    KEY_FILENAME = '<YOUR CLOUD STORAGE SERVICE ACCOUNT>' #Updated Later
    PROJECT_ID = '<YOUR PROJECT ID>'
    ```

## Cloud Run
----
### Main API
- **Build Container Image**
1. Clone recyclops project from github :
    ```
    cd recyclops/'Cloud Computing'/main_api
    ```
2. Build and push image to Container Registry
    ```
    docker build -t gcr.io/$PROJECT_ID/main-api .
    docker push gcr.io/$PROJECT_ID/main-api
    ```
     >__Highly RECOMMENDED container name:__ ***main-api***
- **Deploy Cloud Run**
1. Deploy Main API container image to Cloud Run :
    ```
    gcloud run deploy main-api \
    --image gcr.io/$PROJECT_ID/main-api \
    --region=asia-southeast1 \
    --cpu=1 \
    --memory=512Mi \
    --platform managed
    --min-instances=1 \
    --max-instances=10 \
    --port=8080
    ```
2. Show Main API URL :
    ```
     gcloud run services describe main-api --region asia-southeast1 --format 'value(status.url)'
    ```
    
### Machine Learning API
- **Build Container Image**
1. After deploy Main API in **main_api** folder go to **ml_api** folder :
    ```
    cd ..
    
    export PROJECT_ID=#your GCP project ID
    ```
2. Build and push image to Container Registry
    ```
    docker build -t gcr.io/$PROJECT_ID/ml-api .
    docker push gcr.io/$PROJECT_ID/ml-api
    ```
    >__Highly RECOMMENDED container name:__ ***ml-api***
- **Deploy Cloud Run**
1. Deploy Main API container image to Cloud Run :
    ```
    gcloud run deploy ml-api \
    --image gcr.io/$PROJECT_ID/ml-api \
    --region=asia-southeast1 \
    --cpu=1 \
    --memory=1Gi \
    --platform managed
    --min-instances=1 \
    --max-instances=10 \
    --port=8080
    ```
2. Show ML API URL :
    ```
     gcloud run services describe ml-api --region asia-southeast1 --format 'value(status.url)'
    ```
    
## Firebase
---
__>>Make sure to use SAME ACCOUNT as GOOGLE CLOUD CONSOLE<<__
1. Create a new Firebase project name **SAME** as **Google Cloud Project**.
2. Enable the Realtime Database in the Firebase project.
3. Enable Google Sign-In as the authentication sign-in method in the Firebase project.
4. Generate a Service Account key in the Firebase project.
