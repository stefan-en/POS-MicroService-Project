from datetime import datetime, timedelta

from fastapi.security import OAuth2PasswordBearer
from jose import JWTError, jwt
from passlib.context import CryptContext

from repositories.user_repository import get_user_roles
from repositories.token_repository import*
import uuid

SECRET_KEY = "09d25e094faa6ca2556c818166b7a9563b93f7099f6f0f4caa6cf63b88e8d3e7"
ALGORITHM = "HS256"
ACCESS_TOKEN_EXPIRE_MINUTES = 10080

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")


def user_to_dict(user):
    data = {}
    data["sub"] = user.id
    data["name"] = user.username
    data["roles"] = get_user_roles(user.username)
    return data


def create_access_token(data: dict, expires_delta: timedelta | None = None):
    uid = uuid.uuid4()
    to_encode = data.copy()
    if expires_delta:
        expire = datetime.utcnow() + ACCESS_TOKEN_EXPIRE_MINUTES
    else:
        expire = datetime.utcnow() + timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    to_encode.update({"exp": expire})
    to_encode.update({"iat": datetime.utcnow()})
    to_encode.update({"jti": str(uid)})
    save_token(uid, 1)
    encoded_jwt = jwt.encode(to_encode, SECRET_KEY, algorithm=ALGORITHM)
    return encoded_jwt


def validationJWT(token):
    data = {}
    data["verify_sub"] = False
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM], options=data)
        #de verificat daca e valid in DB
        id = payload.get("sub")
        roles = payload.get("roles")
        name = payload.get('name')
        token_uid = payload.get("jti")
        token_verification = get_token_by_uuid(token_uid)
        if id is None:
            raise "credentials_exception"
    except JWTError:
        return "token invalid la validare"
    if token_verification is not None:
        return f"Tokenul primit este valid in Db + Rolurile lui {name} sunt : {roles} "
    else:
        return f"Tokenul invalid la validare"


def invalidationJWT(token):
    data = {}
    data["verify_sub"] = False
    try:
        payload = jwt.decode(token, SECRET_KEY, algorithms=[ALGORITHM], options=data)
        id = payload.get("sub")
        uid = payload.get("jti")
        #print(this_token.idToken)
        delete_token(uid)
        if id is None:
            raise "credentials_exception"
    except JWTError:
        return "token invalid la invalidare"

    return f"Token cu id: {uid} a fost invalidat"
