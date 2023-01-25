from sqlalchemy import Column, String, Integer
from base.sql_base import Base
from sqlalchemy.orm import relationship

from models.users_roles_orm import user_roles_relationship


# from models.users_roles_orm import users_roles_relationship


class User(Base):
    __tablename__ = 'users'

    id = Column(Integer, primary_key=True)
    username = Column(String)
    password = Column(String)

    roles = relationship("Role", secondary=user_roles_relationship, lazy="dynamic")

    def __init__(self,usernamee, passul):
        self.username = usernamee
        self.password = passul
