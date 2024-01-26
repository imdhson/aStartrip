from bardapi import Bard
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.orm import sessionmaker, declarative_base

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

# all_cards = session.query(Card).all()
# for card in all_cards:
#     print(card.id, card.llm_status, card.card_type)
    
some_cards = session.query(Card).filter_by(llm_status='GENERATING').all() #generating 인 것만 가져오기
for card in some_cards:
    print(card.id, card.llm_status, card.card_type, "바드 질의 시작")
    token = 'fwjgKFvw5mJgOXDiTDzXwIsEpgkdwFQrBsoAhDCc4hutK-RJlUnejL-vsr6IhQp460_LhQ.'
    bard = Bard(token = token)
    response = bard.get_answer(card.user_input0)
    card.llmresponse0 =  response['content']
    card.llm_status= 'COMPLETED'
    print("db에 대입됨: ",response['content'])
session.commit()


