from security.jwt_creator import user_to_dict, create_access_token, SECRET_KEY, validationJWT, invalidationJWT
from repositories.user_repository import get_users, create_user, delete_user, update_user, getUser, getUserByData, \
    update_user_role, getUserByName
from repositories.role_repository import get_roles, get_id
from spyne import String
from spyne import Application, rpc, ServiceBase, Integer
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication

# idm meu ii jwt_creator + toate fisierele de aici
db = get_users()
roles = get_roles()
for user in db:
    print(f"{user.id} {user.username} {user.password}")
for role in roles:
    print(f"{role.id}  {role.rol}")


def getRoles(id):
    for i in roles:
        if i.id == id:
            return i


# serviiu care ia req si trimite jwt la validat-->punctiul central ii GATEWAY(java/python)
# deci din postman -> ma duc in gateway -> la endpointul specific
# deci post mant trimite la gataway(implementez ceva) -> la endpointul respectiv
# in gateway, verific daca rolul jwt validat, e ok si daca se poate face operatiunea x -> apoi abia trimite req catre servicul specific
# fac req si trimit catre ceva

class UserService(ServiceBase):

    @rpc(String, String, _returns=String)
    def login(ctx, username: str, password: str):
        user = getUserByData(username, password)
        if not user:
            return "False user"
        return create_access_token(user_to_dict(user))

    @rpc(String, _returns=String)
    def autentificare(ctx, JWT):  # user
        return validationJWT(JWT)

    @rpc(String, _returns=String)
    def logout(ctx, JWT):  # user
        return invalidationJWT(JWT)

    @rpc(String, String, _returns=String)
    def create(ctx, user, passw):  # create
        create_user(user, passw)
        return f"User-ul: {user} saved"

    @rpc(String, _returns=String)
    def delete(ctx, id):  # delete
        return delete_user(id)

    @rpc(String, String, _returns=String)
    def update(ctx, username, passw):  # update
        return update_user(username, passw)
        # return f"User-ul: {username} si-a schimbat parola"

    @rpc(String, String, _returns=String)
    def update_roles(ctx, username, role):  # update
        return update_user_role(getUserByName(username), get_id(role))
        # return f"User-ul: {username} si-a schimbat parola"

    @rpc(_returns=String)
    def useri(ctx):  # get info id user
        return get_users()


application = Application([UserService], 'services.calculator.soap',
                          in_protocol=Soap11(validator='lxml'),
                          out_protocol=Soap11())
wsgi_application = WsgiApplication(application)

if __name__ == '__main__':
    import logging

    from wsgiref.simple_server import make_server

    logging.basicConfig(level=logging.INFO)
    logging.getLogger('spyne.protocol.xml').setLevel(logging.INFO)

    logging.info("listening to http://127.0.0.1:8000")
    logging.info("wsdl is at: http://127.0.0.1:8000/?wsdl")

    server = make_server('127.0.0.1', 8000, wsgi_application)
    server.serve_forever()
