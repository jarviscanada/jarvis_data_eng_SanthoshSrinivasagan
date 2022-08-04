FROM python:3.8-slim-buster

WORKDIR /app

COPY requirements.txt requirements.txt
RUN pip3 install -r requirements.txt

COPY . .

ENTRYPOINT [ "python3", "python_db_app.py"]

#docker run -itd --network=jarvis-net db-app jrvs--psql 5432 postgres password exercises cd.members Table_out.csv