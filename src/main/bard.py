import sys
from bardapi import Bard
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import sessionmaker, declarative_base
from sqlalchemy import SQLAlchemyError

#DB 연결
engine = create_engine('mysql+pymysql://root:java@cciicc.cc/devas')
Session = sessionmaker(bind=engine)
session = Session()

# Card
Base = declarative_base()
class Card(Base):
    __tablename__ = 'card'
    id = Column(Integer, primary_key = True)
    card_type = Column(String)
    llm_status = Column(String)
    user_input0 = Column(String)
    llmresponse0 = Column(String)
    llmresponse1 = Column(String)
    llmresponse2 = Column(String)


# 전체 인자 리스트 출력
print("Arguments:", sys.argv)
card_id = sys.argv[1]

card = session.query(Card).filter_by(id=card_id).first()
if card.llm_status != 'GENERATING':
    print("false")

token = 'fwjgKFvw5mJgOXDiTDzXwIsEpgkdwFQrBsoAhDCc4hutK-RJlUnejL-vsr6IhQp460_LhQ.'
bard = Bard(token = token)
response = bard.get_answer(card.user_input0)
card.llmresponse0 =  response['content'] 
card.llm_status= 'COMPLETED' # llm status completed로 변경
print("db에 대입됨: ",response['content'])
try:
    session.commit()
except SQLAlchemyError:
    session.rollback()
    print('false')