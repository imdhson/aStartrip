import sys, os
import time
from dotenv import load_dotenv
from bardapi import Bard
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy.exc import SQLAlchemyError

load_dotenv()
# DB 연결
engine = create_engine(os.getenv("DB_ADDRESS"))
Session = sessionmaker(bind=engine)
session = Session()

# Card
Base = declarative_base()


class Card(Base):
    __tablename__ = 'card'
    id = Column(Integer, primary_key=True)
    card_type = Column(String)
    llm_status = Column(String)
    user_input0 = Column(String)
    llmresponse0 = Column(String)
    llmresponse1 = Column(String)
    llmresponse2 = Column(String)

#card_id 를 argv에서 불러옴
card_id = sys.argv[1]
# print("cardID:", card_id)

card = session.query(Card).filter_by(id=card_id, llm_status='GENERATING').first()
if (card == None):
    print('false: cannot find card')
    sys.exit()

bard = Bard(token=os.getenv("BARD_TOKEN"))
card_type_i = card.card_type
if card_type_i == "R01":
    request_i = '''I need to study English. Please generate English paragraph on any topic. It can be long'''
    response = bard.get_answer(request_i)
    card.llmresponse0 = response['content']

elif card_type_i == "R02":
    #Pre-reading question
    request_i = '''Please create a pre-reading question for the paragraph below. Please provide answers in English and Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = response['content']
    time.sleep(3)

    #Backgrond-knowledge
    request_i = '''Please create a background-knowledge for the paragraph below. Please provide answers in English and Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse1 = response['content']
    time.sleep(3)

    #Post-reading question
    request_i = '''Please create a post-reading question for the paragraph below. Please provide answers in English and Korean.
    _________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse2 = response['content']

elif card_type_i == "W01":
    request_i = '''I'm studying English. I'll send you a paragraph, so please evaluate whether the paragraph I sent is appropriate and correct it. Please reply in Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = response['content']

elif card_type_i == "W02":
    request_i = '''I'm studying English. I'll send you a sentence, so please analyze the composition of the sentence. Please reply in Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = response['content']
elif card_type_i == "V01":
    request_i = '''I am studying English. I'll send you the words separated by , so please create an English story using the words. Please reply in Korean.
    ________
    words: ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = response['content']
    
elif card_type_i == "V02":
    #동의어
    request_i = '''I am studying English. I'll send you the sentence, so please find the word in the sentence and let me know the relevant synonyms. Please reply in Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = response['content']
    time.sleep(3)

    #반의어
    request_i = '''I am studying English. I'll send you the sentence, so please find the words in the sentence and tell me the related antonym.Please reply in Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse1 = response['content']
    time.sleep(3)

    #word- family
    request_i = '''I am studying English. I'll send you the sentence, so please find the word in the sentence and tell me the word-family.Please reply in Korean.
    _________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse2 = response['content']
    time.sleep(3)
    
else:
    card.llm_status = "CANCELED"

card.llm_status = 'COMPLETED'  # llm status completed로 변경
# print("db에 대입됨: ", response['content'])
try:
    session.commit()
    print('true')
except SQLAlchemyError:
    session.rollback()
    print('false: commit failed')
