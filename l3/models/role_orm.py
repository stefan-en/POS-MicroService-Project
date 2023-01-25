from sqlalchemy import Column, String, Integer
from sqlalchemy.orm import relationship

from base.sql_base import Base


class Role(Base):
    __tablename__ = 'roles'

    id = Column(Integer, primary_key=True)
    rol = Column(String)

   # roles = relationship("Role", secundary=users_roles_relationship)

    def __init__(self, id, rol):
        self.id = id
        self.rol = rol

