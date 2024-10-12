import sys
import os

# bard api -> gemini -> Perplexity 호환 버전으로 수정

import time
import requests
from dotenv import load_dotenv
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy.exc import SQLAlchemyError


def myReplaceAll(str):
    str = str.replace("**", "")
    str = str.replace("##", "")
    if "|---|---|" in str:  # llm이 표로 답변하면 그냥 에러 상태로 남겨둠
        card.llm_status = "CANCELED"
        session.commit()
        print('false: table detected on llm response')
        sys.exit()
    return str


load_dotenv()
# DB 연결
engine = create_engine(os.getenv("DB_ADDRESS"))
Session = sessionmaker(bind=engine)
session = Session()

# Card
Base = declarative_base()

#Perplexity
pplx_url = "https://api.perplexity.ai/chat/completions"
pplx_headers = {
    "Authorization": "Bearer "+os.getenv("PPLX_API_TOKEN"),
    "Content-Type": "application/json"
}
class Card(Base):
    __tablename__ = 'card'
    id = Column(Integer, primary_key=True)
    card_type = Column(String)
    llm_status = Column(String)
    user_input0 = Column(String)
    llmresponse0 = Column(String)
    llmresponse1 = Column(String)
    llmresponse2 = Column(String)


# card_id 를 argv에서 불러옴
card_id = sys.argv[1]
# print("cardID:", card_id)

card = session.query(Card).filter_by(
    id=card_id, llm_status='GENERATING').first()
if (card == None):
    print('false: cannot find card')
    sys.exit()



card_type_i = card.card_type
if card_type_i == "R01":
    request_i = '''I need to study English. Please generate English paragraph on any topic. It can be long'''
    response = bard.get_answer(request_i)
    card.llmresponse0 = myReplaceAll(response['content'])

elif card_type_i == "R02":
    # Pre-reading question
    request_i = '''Please create a pre-reading question for the paragraph below. Please provide answers in English and Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = myReplaceAll(response['content'])
    time.sleep(3)

    # Backgrond-knowledge

    ________request_i = '''Please create a background-knowledge for the paragraph below. Please provide answers in English and Korean.
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse1 = myReplaceAll(response['content'])
    time.sleep(3)

    # Post-reading question
    request_i = '''Please create a post-reading question for the paragraph below. Please provide answers in English and Korean.
    _________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse2 = myReplaceAll(response['content'])

elif card_type_i == "W01":
    request_i = '''I'm studying English. I'll send you a paragraph, so please evaluate whether the paragraph I sent is appropriate and correct it. Please reply in Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = myReplaceAll(response['content'])

elif card_type_i == "W02":
    request_i = '''I'm studying English. I'll send you a sentence, so please analyze the composition of the sentence. Please reply in Korean.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = myReplaceAll(response['content'])
elif card_type_i == "V01":
    request_i = '''I am studying English. I'll send you the words separated by , so please create an English story using the words.
    ________
    words: ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = myReplaceAll(response['content'])

elif card_type_i == "V02":
    # 동의어
    request_i = '''I am studying English. I'll send you the word, so please find the relevant synonyms word. Find the word for english. and explain in Korean.
    단어를 줄테니 relevant synonyms 찾아주세요. 아래는 단어 입니다.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse0 = myReplaceAll(response['content'])
    time.sleep(3)

    # 반의어
    request_i = '''I am studying English. I'll send you the sentence, so please find the related antonym word. Find the word for english. and explain in Korean.
    단어를 줄테니 antonym 찾아주세요. 아래는 단어 입니다.
    ________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse1 = myReplaceAll(response['content'])
    time.sleep(3)

    # word- family
    request_i = '''I am studying English. I'll send you the sentence, so please find the word-family. Find the word for english. and explain in Korean.
    단어를 줄테니 Word-family 찾아주세요. 아래는 단어 입니다.
    _________
    ''' + card.user_input0
    response = bard.get_answer(request_i)
    card.llmresponse2 = myReplaceAll(response['content'])
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
