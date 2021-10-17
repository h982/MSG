FROM python:3.8.10

ADD . /app 
WORKDIR /app 

RUN python3 -m pip install -U pip 
RUN pip3 install mxnet
RUN pip3 install gluonnlp pandas tqdm
RUN pip3 install sentencepiece
RUN pip3 install transformers==3
RUN pip3 install torch
RUN pip3 install git+https://git@github.com/SKTBrain/KoBERT.git@master

CMD tail -f /dev/null
