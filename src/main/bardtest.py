import sys, os

# bard api -> gemini 호환 버전으로 수정
# sys.path.append("/bardapi")

import time
from dotenv import load_dotenv
from bardapi import Bard
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy.exc import SQLAlchemyError


load_dotenv()

cookie_dict = {
    "__Secure-1PSID": os.getenv("BARD__Secure-1PSID"),
    "__Secure-1PSIDTS": os.getenv("BARD__Secure-1PSIDTS"),
    "__Secure-1PSIDCC": os.getenv("BARD__Secure-1PSIDCC"),
    "NID": os.getenv("BARD__NID"),
    "GOOGLE_ABUSE_EXEMPTION":os.getenv("BARD__GOOGLE_ABUSE_EXEMPTION")
}
token = os.getenv("BARD__Secure-1PSID")

bard = Bard(token=token,cookie_dict=cookie_dict, multi_cookies_bool=True)
print(bard.get_answer("hello. what is your name?")['content'])