#!/usr/bin/env python
# coding: utf-8

# In[4]:


#pip install powernad
#pip install jsonpickle
#pip install python-dotenv


# In[2]:


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

from tqdm.notebook import tqdm
import os


# In[5]:

from dotenv import load_dotenv

load_dotenv()

# get_ipython().run_line_magic('load_ext', 'dotenv')
# get_ipython().run_line_magic('dotenv', '')

# In[6]:

BASE_URL = 'https://api.naver.com'

CUSTOMER_ID = os.environ.get("NAVER_AD_CUSTOMER_ID")
API_KEY = os.environ.get("NAVER_AD_API_KEY")
SECRET_KEY = os.environ.get("NAVER_AD_SECRET_KEY")

client_id = os.environ.get("NAVER_CLIENT_ID")
client_secret = os.environ.get("NAVER_CLIENT_SECRET")


# In[3]:


toda = datetime.now()
time_month = toda - relativedelta(months=1)
time_month = time_month.strftime('%Y-%m-%d')
time_month= str(time_month)


yesterday = toda - relativedelta(days=1)
yesterday = yesterday.strftime('%Y-%m-%d')
yesterday = str(yesterday)

today = str(datetime.now().date())


# In[4]:


dt_index = pd.date_range(start=time_month, end = yesterday)

date = pd.DataFrame(data=dt_index, columns=['날짜'])


# In[7]:


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


# In[20]:


# lis = ['검색하고자 하는 키워드 입력']
lis = ['맥도날드']


# In[43]:


def API(list):
    error=[]
    for i in tqdm(lis):  
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

#     sleep(3)


# In[ ]:


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