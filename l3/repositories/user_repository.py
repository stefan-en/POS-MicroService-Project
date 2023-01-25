from models.user_orm import User
from base.sql_base import Session


def get_users():
    session = Session()
    user = session.query(User).all()
    return user


def getUser(id):
    session = Session()
    user = session.query(User).all()
    for i in user:
        if i.id == id:
            return i


def getUserByName(name):
    ses = Session()
    users = ses.query(User).all()
    for i in users:
        if i.username == name:
            return i


def getUserByData(username, password):
    session = Session()
    user = session.query(User).where(User.username == username and User.password == password).first()
    return user


def get_user_roles(username):
    roles = []
    session = Session()
    user = session.query(User).join(User.roles).where(User.username == username).first()
    if user is None:
        return roles
    for role in user.roles:
        roles.append(role.rol)
    session.close()
    return roles


# value 4 o sa fie admin
def create_user(username, password):
    user = User(username, password)
    try:
        session = Session()
        session.add(user)
        session.commit()
    except Exception as exc:
        print(f"Failed to add user - {exc}")
    return user


def delete_user(id):
    sesion = Session()
    user = sesion.query(User).get(id)
    try:
        sesion.delete(user)
        sesion.commit()
    except Exception as exc:
        print(f"Failed to delete user - {exc}")
    return f"User-ul cu id: {id} a fost sters"


def update_user(username, password):
    sesion = Session()
    user = getUserByName(username)
    new_user = User(user.username, password)
    new_user.id = user.id
    new_user.roles = user.roles
    delete_user(user.id)
    try:
        sesion.add(new_user)
        sesion.commit()
    except Exception as exc:
        print(f"Failed to update user - {exc}")
    return f"User-ul: {new_user.username} si-a schimbat parola"


#cum redirect un req catre celalalt servicviu

def update_user_role(user, new_role):
    sesion = Session()
    roluri_curente = get_user_roles(user.username)
    if new_role in roluri_curente:
        return "Rol existent"
    print(new_role)
    print(user.id)

    try:
        sesion.execute(f"INSERT INTO  `idm`.`users_roles` (`user_id`,`role_id`) VALUES ({user.id}, {new_role})")
        sesion.commit()
    except Exception as exc:
        print(f"Failed to update user - {exc}")
    return f"User-ul: {user.username} are un nou rol"
