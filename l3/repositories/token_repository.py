import models.token_orm
from models.token_orm import Token
from base.sql_base import Session


def get_tokens():
    session = Session()
    role = session.query(Token).all()
    return role


def get_token_by_uuid(uuid):
    sess = Session()
    tokens = sess.query(Token).all()
    for i in tokens:
        if i.idToken == uuid:
            sess.close()
            return i


# value 4 o sa fie admin
def save_token(uuid, valid):
    session = Session()
    token = Token(uuid, valid)
    try:
        session.add(token)
        session.commit()
    except Exception as exc:
        print(f"Failed to add token - {exc}")
    return token


def update_token(uuid, valid):
    session = Session()
    token = get_token_by_uuid(uuid)
    try:
        # session.execute(f"UPDATE `idm`.`tokenData` SET `idm`.`tokenData`.`isValid`= '{valid}' WHERE(`idm`.`tokenData`.`idToken`= '{token.idToken}')")
        # session.query(Token).filter_by(idToken=token.idToken).update({'isValid': valid})
        token.isValid = valid
        session.commit()
    except Exception as exc:
        print(f"Failed to add token - {exc}")
    return token


def delete_token(uuid):
    session = Session()
    token = get_token_by_uuid(uuid)
    if token is not None:
        session.delete(token)
        session.commit()
        print(f"Successfully deleted token with id {uuid}")
    else:
        print(f"Token with id {uuid} not found")
