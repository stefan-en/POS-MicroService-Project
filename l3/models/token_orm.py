from sqlalchemy import Column, String, Integer
from base.sql_base import Base


class Token(Base):
    __tablename__ = 'tokenData'

    idToken = Column(String, primary_key=True)
    isValid = Column(Integer)

    def __init__(self, idTok, valid):
        self.idToken = idTok
        self.isValid = valid
