FROM python:3.8.10

ADD . /app 
WORKDIR /app 

RUN python3 -m pip install -U pip 

RUN pip3 install flask 
RUN pip3 install uwsgi
RUN pip3 install numpy
RUN pip3 install pandas
RUN pip3 install tqdm
RUN pip3 install ijson
RUN pip3 install selenium
RUN pip3 install bs4

RUN pip3 install apscheduler
RUN pip3 install re
RUN pip3 install datetime
RUN pip3 install paramiko
RUN pip3 install scp
RUN pip3 install pymysql

RUN pip3 install powernad
RUN pip3 install jsonpickle
RUN pip3 install python-dotenv
RUN pip3 install ipywidgets
RUN pip3 install requests

RUN pip3 install mxnet
RUN pip3 install gluonnlp
RUN pip3 install sentencepiece
RUN pip3 install transformers==3
RUN pip3 install torch
RUN pip3 install git+https://git@github.com/SKTBrain/KoBERT.git@master

CMD uwsgi uwsgi.ini
