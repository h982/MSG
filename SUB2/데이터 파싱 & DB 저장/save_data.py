from flask import Flask, request
#python mysql 연결 드라이버
import pymysql
import json
import pandas as pd
import os
import shutil
#db 연결 python file
import config

app = Flask(__name__)

DATA_DIR = "./data"
DATA_FILE = os.path.join(DATA_DIR, "data.json")

store_columns = (
    "id",  # 음식점 고유번호
    "store_name",  # 음식점 이름
    "branch",  # 음식점 지점 여부
    "area",  # 음식점 위치
    "tel",  # 음식점 번호
    "address",  # 음식점 주소
    "latitude",  # 음식점 위도
    "longitude",  # 음식점 경도
    "category",  # 음식점 카테고리
)

def import_data(data_path=DATA_FILE):
    
    """
    Req. 1-1-1 음식점 데이터 파일을 읽어서 Pandas DataFrame 형태로 저장합니다
    """
    try:
        with open(data_path, encoding="utf-8") as f:
            data = json.loads(f.read())
    except FileNotFoundError as e:
        print(f + "`{data_path}` 가 존재하지 않습니다.")
        exit(1)

    stores = []  # 음식점 테이블
    reviews = []  # 리뷰 테이블

    for d in data:

        store = {
            "name" : d["name"],
            "area" : d["area"],
            "address" : d["address"],
            "latitude" : d["latitude"],
            "longitude" : d["longitude"]
        }
        setStore(store)

def setStore(store):

    # connetion 가져옴
    conn = config.getConnection()
    # cursor 생성
    cur = conn.cursor()
    # 데이터 입력
    ok = cur.execute("INSERT INTO store(name, area, address, latitude, longitude) VALUES (%s, %s, %s, %s, %s)",
    (store['name'],
    store['area'],store['address'],store['latitude'],store['longitude']))

    # commit
    conn.commit()
    # Connection 닫기
    conn.close()
    # # 처리 결과를 json형식으로 리턴
    # return json.dumps({'rows' : ok})

    return "<p>hello</p>"

@app.route('/')
def parse():
    print("[*] Parsing data...")
    data = import_data()
    print("[+] Done")

if __name__ == '__main__':
	app.run()
