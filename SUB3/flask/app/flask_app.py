#!/usr/bin/env python
# coding: utf-8

from apscheduler.schedulers.background import BackgroundScheduler
from apscheduler.triggers.cron import CronTrigger
#import crawl_sender
import pandas as pd 
import collections
from datetime import timedelta
from dateutil.relativedelta import relativedelta
import numpy as np

import math
from datetime import datetime 
from datetime import datetime, timedelta

from powernad.API.Campaign import *
from powernad.API.RelKwdStat import *

import time

from time import sleep
from urllib.error import HTTPError

import urllib.request
from datetime import datetime, timedelta
import json

from tqdm import tqdm
import os


from dotenv import load_dotenv


##############################NAVER API######################################


load_dotenv()

BASE_URL = 'https://api.naver.com'

CUSTOMER_ID = os.environ.get("NAVER_AD_CUSTOMER_ID")
API_KEY = os.environ.get("NAVER_AD_API_KEY")
SECRET_KEY = os.environ.get("NAVER_AD_SECRET_KEY")

client_id = os.environ.get("NAVER_CLIENT_ID")
client_secret = os.environ.get("NAVER_CLIENT_SECRET")

toda = datetime.now()
time_month = toda - relativedelta(months=1)
time_month = time_month.strftime('%Y-%m-%d')
time_month= str(time_month)


yesterday = toda - relativedelta(days=1)
yesterday = yesterday.strftime('%Y-%m-%d')
yesterday = str(yesterday)

today = str(datetime.now().date())


dt_index = pd.date_range(start=time_month, end = yesterday)

date = pd.DataFrame(data=dt_index, columns=['날짜'])

rel = RelKwdStat(BASE_URL, API_KEY, SECRET_KEY, CUSTOMER_ID)

def search_keyword(searchword):
    

    kwdDataList = rel.get_rel_kwd_stat_list(siteId=None, biztpId=None, hintKeywords=searchword, event=None, month=None, showDetail='1')


    kwd_result = (kwdDataList[0].relKeyword, #키워드
                 kwdDataList[0].monthlyPcQcCnt, #월간 검색수 (PC)
                 kwdDataList[0].monthlyMobileQcCnt, # 월간 검색수 (Mobile)
                 kwdDataList[0].monthlyPcQcCnt+kwdDataList[0].monthlyMobileQcCnt) # 월간 total 

    return(kwd_result[3])


dictionary_yester={}
dictionary_now={}


def API(lists):
    error=[]
    for i in lists:  
        sleep(1)
        try:
            if type(search_keyword(i)) !=str : 
                searchword = i.replace(" ","")
                a = search_keyword(searchword)


                url = "https://openapi.naver.com/v1/datalab/search"
                body = "{\"startDate\":\""+time_month+"\",\"endDate\":\""+today+"\",\"timeUnit\":\"date\",\"keywordGroups\":[{\"groupName\":\""+i+"\",\"keywords\":[\""+i+"\"]}]}";
                requested = urllib.request.Request(url)
                requested.add_header("X-Naver-Client-Id", client_id)
                requested.add_header("X-Naver-Client-Secret", client_secret)
                requested.add_header("Content-Type", "application/json")
                response = urllib.request.urlopen(requested, data=body.encode("utf-8"))
                rescode = response.getcode()

                if(rescode==200):
                    response_body = response.read()
                    output_data = response_body.decode('utf-8')
                else:
                    print('Error code:'+ rescode)
                    pass

                result = json.loads(output_data)


                date = [a['period'] for a in result['results'][0]['data']]


                aa = pd.DataFrame({'date':date, 
                              i:[a['ratio'] for a in result['results'][0]['data']],
                              })

                # 일일 데이터 계산 


                sleep(0.5)
                total = aa[i].sum()


                aa[i] = aa[i].apply(lambda x :((x / total)*float(a)))

            else : 
                pass

        except (TypeError, IndexError,KeyError,ValueError):
            print(" 타입 or 인덱스 에러,Value 에러:",i)
            error.append(i)
            pass


        except HTTPError:
            print('http 에러:', i )
            error.append(i)
            pass

            #여기부터 새로코딩함 
        try: 
            if type(search_keyword(i)) !=str : 
                searchword = i.replace(" ","")
                a = search_keyword(searchword)        

                dt_index = pd.date_range(start=time_month, end= yesterday)
                dt_list = dt_index.strftime("%Y-%m-%d").tolist()

                date = pd.DataFrame(data=dt_list, columns=['날짜'])

                spred = pd.merge(date,aa,left_on='날짜',right_on='date',how='outer')
                spred.drop(['date'],inplace=True, axis=1)

                spred.replace(np.nan,0,inplace=True)

                a=[] 

                for j in range(len(spred)) : 
                    a.append(today)


                spred['수집날짜'] = a

                spred = spred[['날짜','수집날짜',i]]

                if i in dictionary_yester:

                    pass

                else : 
                    dictionary_yester[i]=[]



                dictionary_now[i]=spred

            else:
                pass

        except (TypeError, IndexError,KeyError,ValueError):

            pass

        sleep(0.5)
        try :
            if type(search_keyword(i)) !=str : 
                searchword = i.replace(" ","")
                a = search_keyword(searchword)        

                if dictionary_yester[i]==[]:
                    dictionary_yester[i] = dictionary_now[i]
                   # for k in range(3):
                    #    sleep(1)
                     #   gc2.append_row(dictionary_now[i].iloc[-1-k,:].values.tolist()+[i])


                else : 
                    pass 
            else:
                pass


        except (TypeError, IndexError,KeyError):
              pass


        except ValueError:


                yester_df= dictionary_yester[i]
                today_df = dictionary_now[i]


                yester_df.set_index('날짜',inplace=True)

                today_df.set_index('날짜',inplace=True)

                yester_df.update(today_df)


                tmpt = today_df.iloc[-1,:]

                yester_df = yester_df.append(tmpt)

                yester_df.reset_index(inplace=True)
                today_df.reset_index(inplace=True)

                #dictionary_yester[i] = yester_df

            ## 구글 API로 데이터 올리기
                # 2주간의 데이터만 구글 스프레드 시트에 올리겠다.

              #  for k in range(3):
               #     sleep(1)
                #    gc2.append_row(yester_df.iloc[-1-k,:].values.tolist()+[i])

                dictionary_yester[i] = yester_df


        except :
            print('API 에러',i)
            error.append(i)

##############################NAVER API######################################

##############################koBERT######################################

import os
os.environ["TOKENIZERS_PARALLELISM"] = "false"

import torch
from torch import nn
import torch.nn.functional as F
import torch.optim as optim
from torch.utils.data import Dataset, DataLoader
import gluonnlp as nlp
import numpy as np

from kobert.utils import get_tokenizer
from kobert.pytorch_kobert import get_pytorch_kobert_model

from transformers import AdamW
from transformers.optimization import get_cosine_schedule_with_warmup

device = torch.device('cpu')
bertmodel, vocab = get_pytorch_kobert_model()

class BERTDataset(Dataset):
    def __init__(self, dataset, sent_idx, label_idx, bert_tokenizer, max_len,
                 pad, pair):
        transform = nlp.data.BERTSentenceTransform(
            bert_tokenizer, max_seq_length=max_len, pad=pad, pair=pair)

        self.sentences = [transform([i[sent_idx]]) for i in dataset]
        self.labels = [np.int32(i[label_idx]) for i in dataset]

    def __getitem__(self, i):
        return (self.sentences[i] + (self.labels[i], ))

    def __len__(self):
        return (len(self.labels))

## Setting parameters
max_len = 64
batch_size = 64
warmup_ratio = 0.1
num_epochs = 5
max_grad_norm = 1
log_interval = 200
learning_rate =  5e-5

class BERTClassifier(nn.Module):
    def __init__(self,
                 bert,
                 hidden_size = 768,
                 num_classes=3,
                 dr_rate=None,
                 params=None):
        super(BERTClassifier, self).__init__()
        self.bert = bert
        self.dr_rate = dr_rate
                 
        self.classifier = nn.Linear(hidden_size , num_classes)
        if dr_rate:
            self.dropout = nn.Dropout(p=dr_rate)
    
    def gen_attention_mask(self, token_ids, valid_length):
        attention_mask = torch.zeros_like(token_ids)
        for i, v in enumerate(valid_length):
            attention_mask[i][:v] = 1
        return attention_mask.float()

    def forward(self, token_ids, valid_length, segment_ids):
        attention_mask = self.gen_attention_mask(token_ids, valid_length)
        
        _, pooler = self.bert(input_ids = token_ids, token_type_ids = segment_ids.long(), attention_mask = attention_mask.float().to(token_ids.device))
        if self.dr_rate:
            out = self.dropout(pooler)
        return self.classifier(out)

model = BERTClassifier(bertmodel,  dr_rate=0.5).to(device)
model.load_state_dict(torch.load('./koBERT/model1st.pth',map_location=device))

tokenizer = get_tokenizer()
tok = nlp.data.BERTSPTokenizer(tokenizer, vocab, lower=False)

def predict(predict_sentence):

    result = []
    data = [predict_sentence, '0']
    dataset_another = [data]

    another_test = BERTDataset(dataset_another, 0, 1, tok, max_len, True, False)
    test_dataloader = torch.utils.data.DataLoader(another_test, batch_size=batch_size, num_workers=2)
    
    model.eval()

    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(test_dataloader):
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)

        valid_length= valid_length
        label = label.long().to(device)

        out = model(token_ids, valid_length, segment_ids)

        test_eval=[]
        logits=out
        logits = logits.detach().cpu().numpy()

        return np.argmax(logits)

def predictMany(predict_sentence):
    result = []
    dataset_another = []
    for sen in predict_sentence:
      data = [sen, '0']
      dataset_another.append(data)

    another_test = BERTDataset(dataset_another, 0, 1, tok, max_len, True, False)
    test_dataloader = torch.utils.data.DataLoader(another_test, batch_size=batch_size, num_workers=2)
    
    model.eval()

    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(test_dataloader):
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)

        valid_length= valid_length
        label = label.long().to(device)

        out = model(token_ids, valid_length, segment_ids)


        test_eval=[]
        for i in out:
            logits=i
            logits = logits.detach().cpu().numpy()
            result.append(np.argmax(logits))
    return result


##############################koBERT######################################

##############################mySQL##########################################

import pymysql
import sys
import pandas as pd
from ast import literal_eval
import random

def insert(df){
    csv_data = df;
    port = "3306"
    #DB Connection 생성
    conn = pymysql.connect(host = "j5c101.p.ssafy.io", user = "root", passwd="white123", db="trend", charset='utf8mb4')
    cursor = conn.cursor()

    query = "INSERT INTO google (gid, google_keyword,google_review_date,google_stars,google_star_avg,google_review_txt,google_emotion) VALUES (%s,%s,%s,%s,%s,%s,%s)"
    
    csv_data = csv_data.loc[~csv_data['google_review_date'].isnull()]
    csv_data = csv_data.loc[~csv_data['google_stars'].isnull()]
    csv_data = csv_data.loc[~csv_data['google_review_txt'].isnull()]
    csv_data = csv_data.loc[~csv_data['google_star_avg'].isnull()]
    
    list = []
    
    for keyword, date, stars, avg, txt in zip(csv_data['google_keyword'],csv_data['google_review_date'],csv_data['google_stars'],csv_data['google_star_avg'],csv_data['google_review_txt']):
      for d, s, t, p in zip(date,stars,txt,predict):
        temp = []
        a = random.randint(0,99999999999999999)
        temp.append(a)
        temp.append(keyword)
        temp.append(d)
        temp.append(int(s[4:5]))
        temp.append(avg)
        temp.append(t)
        temp.append(p)
        list.append(temp)
    
    cursor.executemany(query, list_q)
    conn.commit()
}


##############################mySQL##########################################


##############################GOOGLE MAP######################################

from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException
from selenium.common.exceptions import NoSuchElementException
from selenium.webdriver.chrome.options import Options 
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from bs4 import BeautifulSoup
from tqdm import tqdm
import pandas as pd
import ijson
import numpy as np
import time

def crawling():

    chromedriver='./chromedriver'
    chrome_options = webdriver.ChromeOptions()
    chrome_options.add_argument('--headless')
    chrome_options.add_argument('--no-sandbox')
    chrome_options.add_argument('--disable-dev-shm-usage')
    
    driver = webdriver.Chrome(chromedriver,options=chrome_options)
    time.sleep(1)

    df = pd.read_csv('./crawl/google_map_data/GoogleMap용_가게정보_part_3.csv', sep=',', encoding='utf-8')
    #start = 0
    #end = start + 10

    #df = df[start:end]

    review_stars_list = [] # 개별 평점
    review_time_list = [] # 개별 리뷰 작성 시간
    review_list = []
    review_emot = []
    star_avg_list = []
    
    for i, keyword in enumerate(tqdm(df['google_keyword'])):
        
        google_map_search_url = f"https://www.google.com/maps/search/{keyword}"
        driver.get(google_map_search_url)
        time.sleep(1)
        try:
            # 별점 
            star_review_stars = driver.find_element_by_xpath("/html/body/div[3]/div[9]/div[8]/div/div[1]/div/div/div[2]/div[1]/div[1]/div[2]/div/div[1]/div[2]/span/span/span").text
            
            more_reviews = driver.find_element_by_xpath("/html/body/div[3]/div[9]/div[8]/div/div[1]/div/div/div[2]/div[1]/div[1]/div[2]/div/div[1]/span[1]/span/span[1]/span[2]/span[1]/button")
            more_reviews.send_keys(Keys.ENTER)
            time.sleep(2)
           
            # 리뷰개수만큼 스크롤
            #Find the total number of reviews
            total_number_of_reviews = driver.find_element_by_xpath('//*[@id="pane"]/div/div[1]/div/div/div[2]/div[2]/div/div[2]/div[2]').text.split(" ")[1][:-1]
            total_number_of_reviews = int(total_number_of_reviews.replace(',','')) if ',' in total_number_of_reviews else int(total_number_of_reviews)
            #Find scroll layout
            scrollable_div = driver.find_element_by_xpath('//*[@id="pane"]/div/div[1]/div/div/div[2]')
            #Scroll as many times as necessary to load all reviews
            for i in range(0,(round(total_number_of_reviews/10 - 1))):
                    driver.execute_script('arguments[0].scrollTop = arguments[0].scrollHeight', 
                            scrollable_div)
                    time.sleep(1)

            review_text_list = [] # 임시 선언

            response = BeautifulSoup(driver.page_source, 'html.parser')
            result_set = response.find_all('div', class_='ODSEW-ShBeI NIyLF-haAclf gm2-body-2')
            rev_dict = {'Review Rate': [],
                'Review Time': [],
                'Review Text' : [],
                'Review Emotion' : []
                }
            
            for result in result_set:
                review_rate = result.find('span', class_='ODSEW-ShBeI-H1e3jb')["aria-label"]
                review_time = result.find('span',class_='ODSEW-ShBeI-RgZmSc-date').text
                review = result.find('span',class_='ODSEW-ShBeI-text').text
                rev_dict['Review Rate'].append(review_rate)
                rev_dict['Review Time'].append(review_time)
                rev_dict['Review Text'].append(review)
                #rev_dict['Review Emotion'].append(predict(review)) # kobert 예측부
                review_text_list.append(review)
            rev_dict['Review Emotion'].append(predictMany(review_text_list))
            res = pd.DataFrame(rev_dict)
    #         print(res)
            review_stars_list.append(rev_dict['Review Rate'])
            review_time_list.append(rev_dict['Review Time'])
    #         review_text = ','.join(review_text_list)
            review_list.append(rev_dict['Review Text']) # 일단 join 하지말고 list로
            review_emot.append(rev_dict['Review Emotion'])
            star_avg_list.append(star_review_stars)

        # 리뷰가 없는 업체는 크롤링에 오류가 뜨므로 표기해둡니다.
        except Exception as e1:
            print(f"{i}행 문제가 발생")
            print(e1)
            # 리뷰가 없으므로 null을 임시로 넣어줍니다.
            review_stars_list.append('null')
            review_time_list.append('null')
            review_list.append('null')
            review_emot.append('null')
            star_avg_list.append('null')
            
    # driver.quit()
    df['google_review_date'] = review_time_list # 리뷰 작성 시간
    df['google_stars'] = review_stars_list
    df['google_star_avg'] = star_avg_list  # 상세페이지에서 평가한 별점 평균
    df['google_review_txt'] = review_list  # 상세페이지에 나온 리뷰 텍스트들
    df['google_emotion'] = review_emot
    
    insert(df);
    
    # ElasticSearch용
    import os
    from datetime import datetime

    temp = df[['name','area', 'address','latitude','longitude']]
    today = str(datetime.date(datetime.today()))
    folder = './crawl/outputs/test/'
    file = 'ES_' + str(start) + '~' + str(end) + '행_' + today + '.csv'

    if os.path.isfile(file):
        os.remove(file)
    temp.to_csv(folder + file, encoding='utf-8')
    # df.to_csv(today + '_test.csv', encoding='cp949')
    print(file)

    # Bert용

    temp = df[['google_keyword', 'google_review_date','google_stars','google_star_avg','google_review_txt','google_emotion']]
    today = str(datetime.date(datetime.today()))
    folder = './crawl/outputs/test/'
    file = 'BERT_' + str(start) + '~' + str(end) + '행_' + today + '.csv'

    if os.path.isfile(file):
        os.remove(file)
    temp.to_csv(folder + file, encoding='utf-8')
    print(file)
    
    #reviewProcessor = ReviewPreprocessor("ec2 clu2 주소", "j5c501")
    #reviewProcessor.process("걔 위치")
    
    
##############################GOOGLE MAP######################################






##############################Sender ########################################


sched = BackgroundScheduler(daemon=True)
#sched.add_job(crawling,'cron', minutes=5)
sched.add_job(crawling,'cron', day_of_week='wed',hour='0')
sched.start()

import flask
from flask import request, jsonify

app= flask.Flask(__name__)
# app.config["DEBUG"] =True #디버깅 모드
app.config['JSON_AS_ASCII'] =False #한글이 깨져서 아스키코드가 아니라고 선언

@app.route('/',methods=['GET'])
def home():
    return "<h1>My first Flask!</h1>"

@app.route('/searchAPI')
def search():
    param = request.args.get('keyword')
    print("검색량 키워드: " + param)
    lis = []
    lis.append(param)
    API(lis)
    temp = dictionary_now[param][param].tolist()
    temp = [int(temp) for temp in temp]
    return jsonify(temp)
    
if __name__ == '__main__':
    app.run(debug=False,host='0.0.0.0',port=5000)


# In[ ]:

"""
import matplotlib.pyplot as plt
import matplotlib.dates as mdates

datelist = dictionary_now['맥도날드']['날짜'][:-1]

x=datelist.tolist()
y=temp


plt.figure(figsize=(10,7))
plt.plot(x,y,marker="o")
plt.xticks(rotation=45)
plt.show()

"""