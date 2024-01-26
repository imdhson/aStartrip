from bardapi import Bard
from sqlalchemy import create_engine, Column, Integer, String
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

#DB 연결
engine = create_engine('mysql+pymysql://root:java@cciicc.cc/devas')
Session = sessionmaker(bind=engine)
session = Session()


# Card
Base = declarative_base()
class Card(Base):
    __tablename__ = 'card'
    id = Column(Integer, primary_key = True)
    cardType = Column(String)
    llmStatus = Column(String)
    cardType = Column(String)
    UserInput0 = Column(String)
    LLMResponse0 = Column(String)
    LLMResponse1 = Column(String)
    LLMResponse2 = Column(String)

all_cards = session.query(Card).all()
for card in all_cards:
    print(card.id, card.llmStatus, card.cardType)



token = 'fwjgKFvw5mJgOXDiTDzXwIsEpgkdwFQrBsoAhDCc4hutK-RJlUnejL-vsr6IhQp460_LhQ.'
bard = Bard(token = token)
response = bard.get_answer("베이비몬스터 아현 복귀 소식")
print(response['content'])