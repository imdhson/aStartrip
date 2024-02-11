import sys, os

#bard api -> gemini 호환 버전으로 수정
sys.path.append("/bardapi")

import time
from dotenv import load_dotenv
from bardapi import Bard
from bardapi import BardCookies
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

bard = BardCookies(cookie_dict=cookie_dict)
print(bard.get_answer("hello. what is your name?")['content']) ##111111