import os
import sys
import pandas as pd
import re
from datetime import datetime
import paramiko 
from scp import SCPClient, SCPException

class ReviewPreprocessor:
    def __init__(self, hostname, username):
        self.hangul = re.compile('[^ 가-힣+]')
        self.ssh_manager = SSHManager()
        self.ssh_manager.create_ssh_client(hostname, username)
        self.dir_path = "hdfs/" + datetime.today().strftime("%Y-%m-%d")
        self.remote_path = "/home/j5c101/crawl/" + datetime.today().strftime("%Y-%m-%d")
        if(not os.path.isdir(self.dir_path)):
            os.mkdir(self.dir_path)
        self.ssh_manager.mkdir(self.remote_path)
        
    def process(self, file_path):
        google = pd.read_csv(file_path)

        file_names = list()
        for i in range(len(google)):
            temp = google.loc[i]
            reviews = self.cleanReview(temp.google_review_txt)
            file_names.append(self.writeReviews(temp.google_keyword, reviews))
        self.sshOperation(file_names)

    def cleanReview(self, reviews):
        review_list = reviews[1:-1].split(',')
        ret_list = list()

        for i in range(len(review_list)):
            clean = self.hangul.sub('', review_list[i])
            spacing = ' '.join(clean.split())
            if len(spacing) != 0:
                ret_list.append(spacing)

        return ret_list


    def writeReviews(self, store_name, reviews):
        file_name = store_name + '.txt'
        with open(os.path.join(self.dir_path, file_name), 'w') as file:
            for review in reviews:
                file.write(review + '\n')
        return file_name
                
    def sshOperation(self, file_names):
        for i, file_name in enumerate(file_names):
            file_path = os.path.join(self.dir_path, file_name)
            self.ssh_manager.send_file(file_path, self.remote_path)

class SSHManager: 
    def __init__(self):
        self.ssh_client = None 
        
    def create_ssh_client(self, hostname, username):
        """Create SSH client session to remote server""" 
        if self.ssh_client is None: 
            self.ssh_client = paramiko.SSHClient() 
            self.ssh_client.set_missing_host_key_policy(paramiko.AutoAddPolicy()) 
            self.ssh_client.connect(hostname, username=username) 
        else: 
            print("SSH client session exist.") 
            
    def close_ssh_client(self): 
        self.ssh_client.close() 
        
    def send_file(self, local_path, remote_path): 
        try: 
            with SCPClient(self.ssh_client.get_transport()) as scp: 
                scp.put(local_path, remote_path, preserve_times=True) 
        except SCPException: 
            raise SCPException.message
    
    def mkdir(self, dir_path):
        stdin, stdout, stderr = self.ssh_client.exec_command("mkdir " + dir_path)
        lines = stdout.readlines()
        for line in lines:
            print(str(line).replace('\n',''))

# host, user, file_name
if __name__ == '__main__':
    if len(sys.argv) != 4:
        print("<hostname> <username> <file_name>")
    
    reviewProcessor = ReviewPreprocessor(sys.argv[1], sys.argv[2])
    reviewProcessor.process(sys.argv[3])
        
