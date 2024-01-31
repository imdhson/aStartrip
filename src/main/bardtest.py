import sys, os
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
}

bard = BardCookies(cookie_dict=cookie_dict)
print(bard.get_answer("こんにちは")['content']) ##111111