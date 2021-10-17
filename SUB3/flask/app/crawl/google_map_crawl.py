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

chromedriver='/app/chromedriver'
chrome_options = webdriver.ChromeOptions()
chrome_options.add_argument('--headless')
chrome_options.add_argument('--no-sandbox')
chrome_options.add_argument('--disable-dev-shm-usage')
driver = webdriver.Chrome(chromedriver,chrome_options=chrome_options)
time.sleep(1)

df = pd.read_csv('/app/crawl/google_map_data/2021-09-24_부분_0~55391.csv', sep=',', encoding='utf-8')
start = 18001
end = 55391

df = df[start:end]
#print(df)
#df = df[3:10]

df.rename(columns={"naver_keyword":"google_keyword"},inplace=True)

review_stars_list = [] # 개별 평점
review_time_list = [] # 개별 리뷰 작성 시간
review_list = []
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
        # 스크롤 안됨

        review_text_list = [] # 임시 선언

        response = BeautifulSoup(driver.page_source, 'html.parser')
        result_set = response.find_all('div', class_='ODSEW-ShBeI NIyLF-haAclf gm2-body-2')
        rev_dict = {'Review Rate': [],
            'Review Time': [],
            'Review Text' : []}
        
        for result in result_set:
            review_rate = result.find('span', class_='ODSEW-ShBeI-H1e3jb')["aria-label"]
            review_time = result.find('span',class_='ODSEW-ShBeI-RgZmSc-date').text
            review = result.find('span',class_='ODSEW-ShBeI-text').text
            rev_dict['Review Rate'].append(review_rate)
            rev_dict['Review Time'].append(review_time)
            rev_dict['Review Text'].append(review)
            review_text_list.append(review)
        res = pd.DataFrame(rev_dict)
#         print(res)
        review_stars_list.append(rev_dict['Review Rate'])
        review_time_list.append(rev_dict['Review Time'])
#         review_text = ','.join(review_text_list)
        review_list.append(rev_dict['Review Text']) # 일단 join 하지말고 list로
        star_avg_list.append(star_review_stars)

    # 리뷰가 없는 업체는 크롤링에 오류가 뜨므로 표기해둡니다.
    except Exception as e1:
        print(f"{i}행 문제가 발생")
        print(e1)
        # 리뷰가 없으므로 null을 임시로 넣어줍니다.
        review_stars_list.append('null')
        review_time_list.append('null')
        review_list.append('null')
        star_avg_list.append('null')
        
# driver.quit()
df['google_review_date'] = review_time_list # 리뷰 작성 시간
df['google_stars'] = review_stars_list
df['google_star_avg'] = star_avg_list  # 상세페이지에서 평가한 별점 평균
df['google_review_txt'] = review_list  # 상세페이지에 나온 리뷰 텍스트들

# df[df['google_star_point'] == 'null']
df = df.loc[df['google_review_txt'] != 'null']

#%cd outputs/ES용 data
# ElasticSearch용
import os
from datetime import datetime

temp = df[['name','area', 'address','latitude','longitude']]
today = str(datetime.date(datetime.today()))
file = 'ES_' + str(start) + '~' + str(end) + '행_' + today + '.csv'

if os.path.isfile(file):
    os.remove(file)
temp.to_csv(file, encoding='utf-8')
# df.to_csv(today + '_test.csv', encoding='cp949')
print(file)

#%cd ../BERT용 data
# Bert용

temp = df[['name', 'naver_star_point','naver_blog_review_txt']]
today = str(datetime.date(datetime.today()))
file = 'BERT_' + str(start) + '~' + str(end) + '행_' + today + '.csv'

if os.path.isfile(file):
    os.remove(file)
temp.to_csv(file, encoding='utf-8')
# df.to_csv(today + '_test.csv', encoding='cp949')
print(file)