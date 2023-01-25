# from http.server import BaseHTTPRequestHandler
#
# import requests
#
#
import requests


def creare_cerere_login(user, passw):
    headers = {'content-type': 'application/soap+xml'}
    body = f"""
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                  xmlns:w="services.calculator.soap">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <w:login>
                            <w:username>{user}</w:username>
                            <w:password>{passw}</w:password>
                        </w:login>
                    </soapenv:Body>
                </soapenv:Envelope>
                """
    return body, headers


def parsare_post_login(string):
    start = string.index('<tns:loginResponse>')
    end = string.index('</tns:loginResponse>')
    rez = string[start:end].replace('<tns:loginResponse><tns:loginResult>', '').replace("</tns:loginResult>", '')
    return rez


def parsare_post_validare(string):
    start = string.index(' [')
    end = string.index(' <')
    rez = string[start + 2:end - 1]
    # ceva = rez.inde
    return rez


def parsare_post_validare_update(string):
    start = string.index('lui ')
    end = string.index(' sunt')
    return string[start + len('lui: ') - 1:end]


def creare_cerere_validare(jwt):
    headers = {'content-type': 'application/soap+xml'}
    body = f"""
                        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                          xmlns:w="services.calculator.soap">
                            <soapenv:Header/>
                            <soapenv:Body>
                                <w:autentificare>
                                    <w:JWT>{jwt}</w:JWT>
                                </w:autentificare>
                            </soapenv:Body>
                        </soapenv:Envelope>
                        """
    return body, headers


def creare_cerere_adaugare_rol(username, role):
    headers = {'content-type': 'application/soap+xml'}
    body = f"""
                        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                          xmlns:w="services.calculator.soap">
                            <soapenv:Header/>
                            <soapenv:Body>
                                <w:update_roles>
                                    <w:username>{username}</w:username>
                                    <w:role>{role}</w:role>
                                </w:update_roles>
                            </soapenv:Body>
                        </soapenv:Envelope>
                        """
    return body, headers


def creare_utilizator(user, passw):
    headers = {'content-type': 'application/soap+xml'}
    body = f"""
                           <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                  xmlns:w="services.calculator.soap">
    <soapenv:Header/>
    <soapenv:Body>
        <w:create>
            <w:user>{user}</w:user>
            <w:passw>{passw}</w:passw>
        </w:create>
    </soapenv:Body>
</soapenv:Envelope>
                            """
    return body, headers


def string_to_list(string):
    return string.split(", ")


def delete_utilizator(id):
    headers = {'content-type': 'application/soap+xml'}
    body = f"""
                               <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:w="services.calculator.soap">
        <soapenv:Header/>
        <soapenv:Body>
            <w:delete>
                <w:id>{id}</w:id>
            </w:delete>
        </soapenv:Body>
    </soapenv:Envelope>
                                """
    return body, headers


def update_data_user(username, passw):
    headers = {'content-type': 'application/soap+xml'}
    body = f"""
                               <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                      xmlns:w="services.calculator.soap">
        <soapenv:Header/>
        <soapenv:Body>
            <w:update>
                <w:username>{username}</w:username>
                <w:passw>{passw}</w:passw>
            </w:update>
        </soapenv:Body>
    </soapenv:Envelope>
                                """
    return body, headers


def cerere_get_user():
    headers = {'content-type': 'application/soap+xml'}
    body = f"""
                                   <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                          xmlns:w="services.calculator.soap">
            <soapenv:Header/>
            <soapenv:Body>
                <w:useri>
                </w:useri>
            </soapenv:Body>
        </soapenv:Envelope>
                                    """
    return body, headers


def autorizare_admin(list):
    rez = False
    for i in range(0, len(list)):
        if list[i] == "'admin'":  # atentie la spatiile sus
            rez = True
    return rez


def autorizare_content_manager(list):
    rez = False
    for i in range(0, len(list)):
        if list[i] == "'content_manager'":  # atentie la spatiile sus
            rez = True
    return rez


def autorizare_artist(list):
    rez = False
    for i in range(0, len(list)):
        if list[i] == "'artist'":  # atentie la spatiile sus
            rez = True
    return rez


def autorizare_client(list):
    rez = False
    for i in range(0, len(list)):
        if list[i] == "'client'":  # atentie la spatiile sus
            rez = True
    return rez
