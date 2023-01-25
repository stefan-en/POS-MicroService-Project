from models.role_orm import Role
from base.sql_base import Session
from repositories.user_repository import getUserByName


def get_roles():
    session = Session()
    roles = session.query(Role).all()
    return roles


def get_id(rol):
    for this in get_roles():
        if this.rol == rol:
            return this.id


def get_role_user(user):
    user = getUserByName(user)
    list = []
    for role in user.roles:
        list.append(role)
    return list


# value 4 o sa fie admin
def create_role(id, value):
    session = Session()
    role = Role(id, value)
    try:
        session.add(role)
        session.commit()
    except Exception as exc:
        print(f"Failed to add user - {exc}")
    return role
