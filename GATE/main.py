import json

import flask
import requests
import xmltodict

from flask import Flask, request, make_response, jsonify
from flask_cors import cross_origin, CORS

from request_to_idm.login import creare_cerere_login, creare_cerere_validare, parsare_post_validare, \
    string_to_list, autorizare_admin, creare_utilizator, delete_utilizator, parsare_post_validare_update, \
    update_data_user, autorizare_content_manager, creare_cerere_adaugare_rol, autorizare_artist, autorizare_client, \
    cerere_get_user

url_get_artist = 'http://localhost:8090/api/songcollection/artists'
url_post_artist = 'http://localhost:8090/api/songcollection/artists/save'
url_delete_artist = 'http://localhost:8090/api/songcollection/artists/delete'

url_get_songs = 'http://localhost:8090/api/songcollection/songs'
url_delete_songs = 'http://localhost:8090/api/songcollection/songs/delete'
url_post_songs = 'http://localhost:8090/api/songcollection/songs/save'

url_get_playlist = 'http://localhost:8080/api/playlistcollection/playlists'
url_post_playlist = 'http://localhost:8080/api/playlistcollection/playlists/save'
url_delete_playlist = 'http://localhost:8080/api/playlistcollection/playlists/delete'

app = Flask(__name__)
CORS(app, resources={r"/*": {"origins": "*"}})


@app.route("/artists", methods=["OPTIONS"])
def api_create_order1():
    if request.method == "OPTIONS":  # CORS preflight
        return _build_cors_preflight_response()


@app.route("/songs", methods=["OPTIONS"])
def api_create_order2():
    if request.method == "OPTIONS":  # CORS preflight
        return _build_cors_preflight_response()


@app.route("/playlists", methods=["OPTIONS"])
def api_create_order3():
    if request.method == "OPTIONS":  # CORS preflight
        return _build_cors_preflight_response()


def _build_cors_preflight_response():
    response = make_response()
    response.headers.add("Access-Control-Allow-Origin", "*")
    response.headers.add('Access-Control-Allow-Headers', "*")
    response.headers.add('Access-Control-Allow-Methods', "*")
    return response


@app.route('/login', methods=['POST'])
@cross_origin()
def login():
    if request.method == "POST" and request.query_string.decode() == 'token':
        try:
            data = request.get_json()
            username = data['username']
            password = data['password']
            body, headers = creare_cerere_login(username, password)
            url = 'http://localhost:8000'
            response = requests.post(url, data=body, headers=headers)
            resp = make_response(response.content, 202)
            resp.headers['content-type'] = 'text/xml'
            return xmltodict.parse(response.text)
        except:
            print("Ai probleme la GetLogin")
    elif request.method == "POST":
        url = 'http://localhost:8000'
        jwt = request.headers['Authorization']
        body, headers = creare_cerere_validare(jwt)
        response = requests.post(url, data=body, headers=headers)
        rep1 = parsare_post_validare(response.text)
        print(rep1)  # Tokenul primit este valid in Db
        return response.json()


@app.route('/users', methods=['GET', 'POST', 'PUT', 'DELETE'])
def user_crud():
    if request.method == 'POST':
        url = 'http://localhost:8000'
        data = request.get_json()
        username = data['username']
        password = data['password']
        if 'Authorization' in request.headers:
            jwt = request.headers['Authorization']

            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)
            try:
                rep1 = parsare_post_validare(response.text)
            except:
                print("Nu ai token")
                return make_response(
                    jsonify({'message': 'Neautorizat-nu ai token'}),
                    403)
            roles = string_to_list(rep1)
            drept = autorizare_admin(roles)

            if drept:
                body2, headers2 = creare_utilizator(username, password)
                response2 = requests.post(url, data=body2, headers=headers2)
                return xmltodict.parse(response2.text)
        else:
            return make_response(
                jsonify({'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}),
                401)
    elif request.method == 'DELETE':
        url = 'http://localhost:8000'
        data = request.get_json()
        id = data['id']
        if 'Authorization' in request.headers:
            jwt = request.headers['Authorization']

            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)

            try:
                rep1 = parsare_post_validare(response.text)
            except:
                print("Nu ai token")
                return make_response(
                    jsonify({'message': 'Neautorizat-nu ai token'}),
                    403)
            roles = string_to_list(rep1)
            drept = autorizare_admin(roles)

            if drept:
                body2, headers2 = delete_utilizator(id)
                response2 = requests.post(url, data=body2, headers=headers2)
                return response2.text
            else:
                print(f"User don't have admin right")
        else:
            return make_response(
                jsonify({'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}),
                401)
    elif request.method == 'PUT':  # schimb parola
        if request.query_string.decode() == 'roles':  # adaug roluri
            url = 'http://localhost:8000'
            data = request.get_json()
            username = data['username']
            role = data['role']
            if 'Authorization' in request.headers:
                jwt = request.headers['Authorization']

                body, headers = creare_cerere_validare(jwt)
                response = requests.post(url, data=body, headers=headers)

                try:
                    rep1 = parsare_post_validare(response.text)
                except:
                    print("Nu ai token")
                    return make_response(
                        jsonify({'message': 'Neautorizat-nu ai token'}),
                        403)
                roles = string_to_list(rep1)

                drept = autorizare_admin(roles)

                if drept:
                    body2, headers2 = creare_cerere_adaugare_rol(username, role)
                    response2 = requests.post(url, data=body2, headers=headers2)
                    return response2.text

                else:
                    print(f"User don't have admin right")
            else:
                return make_response(
                    jsonify(
                        {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}),
                    401)
        else:
            url = 'http://localhost:8000'
            data = request.get_json()
            user = data['username']
            passw = data['password']
            if 'Authorization' in request.headers:
                jwt = request.headers['Authorization']

                body, headers = creare_cerere_validare(jwt)
                response = requests.post(url, data=body, headers=headers)
                try:
                    rep1 = parsare_post_validare_update(response.text)
                except:
                    print("Nu ai token")
                    return make_response(
                        jsonify({'message': 'Neautorizat-nu ai token'}),
                        403)

                if response.status_code == 200 and (rep1 == user):
                    body2, headers2 = update_data_user(user, passw)
                    response2 = requests.post(url, data=body2, headers=headers2)
                    return response2.text
                else:
                    return "Nu se poate actualiza parola. Informatii invalide"
            return make_response(
                jsonify(
                    {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}),
                401)
    elif request.method == 'GET':
        url = 'http://localhost:8000'
        print(request.get_json())
        print(request.headers)
        print(request.data)
        if 'Authorization' in request.headers:
            jwt = request.headers['Authorization']
            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)
            if response.status_code == 200:
                body2, headers2 = cerere_get_user()
                response2 = requests.post(url, data=body2, headers=headers2)
                return xmltodict.parse(response2.text)
        return make_response(
            jsonify(
                {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}),
            401)


@app.route('/artists/', methods=['GET', 'POST', 'DELETE'])
def crud_artist():
    url = 'http://localhost:8000'
    if request.method == 'GET':
        # print(request.query_string)
        # print(request.query_string.decode())
        # print(request.path)
        # print(request.full_path)
        if request.query_string != "":
            if 'Authorization' in request.headers:
                jwt = request.headers['Authorization']
                body, headers = creare_cerere_validare(jwt)
                response = requests.post(url, data=body, headers=headers)
                try:
                    rep1 = parsare_post_validare(response.text)
                except:
                    print("Nu ai token")
                    return make_response(
                        jsonify({'message': 'Neautorizat-nu ai token'}),
                        403)
                roles = string_to_list(rep1)
                drept = autorizare_content_manager(roles)
                if drept:
                    response2 = requests.get(url_get_artist + "?" + request.query_string.decode(), data=body,
                                             headers=request.headers)
                else:
                    print(f"User don't have admin right")
                # response3 = flask.jsonify(json.loads(response2.json()))
                # return response3
                return response2.json()
            return make_response(
                jsonify(
                    {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}),
                401)

    elif request.method == 'POST':
        # body  = json  = req.json if req.json else None
        if 'Authorization' in request.headers:
            jwt = request.headers['Authorization']
            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)
            try:
                rep1 = parsare_post_validare(response.text)
            except:
                print("Nu ai token")
                return make_response(
                    jsonify({'message': 'Neautorizat-nu ai token'}),
                    403)
            roles = string_to_list(rep1)
            drept = autorizare_content_manager(roles)

            if drept:
                response2 = requests.post(url_post_artist, data=json.dumps(request.get_json()), headers=request.headers)
            else:
                print(f"User don't have admin right")
            return response2.json()
        return make_response(
            jsonify(
                {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}),
            401)
    elif request.method == 'DELETE':
        # body  = json  = req.json if req.json else None
        if 'Authorization' in request.headers:
            jwt = request.headers['Authorization']
            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)
            try:
                rep1 = parsare_post_validare(response.text)
            except:
                print("Nu ai token")
                return make_response(
                    jsonify({'message': 'Neautorizat-nu ai token'}),
                    403)
            roles = string_to_list(rep1)
            drept = autorizare_content_manager(roles)

            if drept:
                response2 = requests.delete(url_delete_artist, data=json.dumps(request.get_json()),
                                            headers=request.headers)
            else:
                print(f"User don't have admin right")
            return response2.text
        return make_response(
            jsonify(
                {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}),
            401)


@app.route('/songs/', methods=['GET', 'POST', 'DELETE', 'PUT'])
def crud_songs():
    url = 'http://localhost:8000'
    if request.method == 'GET':  # done
        if len(request.query_string) == 0:  # getclasic
            if 'Authorization' in request.headers:
                jwt = request.headers['Authorization']
                body, headers = creare_cerere_validare(jwt)
                response = requests.post(url, data=body, headers=headers)
                if response.status_code == 200:
                    try:
                        rep1 = parsare_post_validare(response.text)
                    except:
                        print("Nu ai token")
                        return make_response(
                            jsonify({'message': 'Neautorizat-nu ai token'}),
                            403)
                    roles = string_to_list(rep1)
                    drept_manager = autorizare_content_manager(roles)
                    if drept_manager:
                        response2 = requests.get(url_get_songs, data=body, headers=request.headers)
                    else:
                        print(f"User don't have admin right")
                    return response2.json()
            else:
                return make_response(jsonify(
                    {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}), 401)
        elif len(request.query_string) != 0:
            if 'Authorization' in request.headers:
                jwt = request.headers['Authorization']
                body, headers = creare_cerere_validare(jwt)
                response = requests.post(url, data=body, headers=headers)
                try:
                    rep1 = parsare_post_validare(response.text)
                except:
                    print("Nu ai token")
                    return make_response(
                        jsonify({'message': 'Neautorizat-nu ai token'}),
                        403)
                roles = string_to_list(rep1)
                drept_manager = autorizare_content_manager(roles)
                print(url_get_songs + "?" + request.query_string.decode())
                if drept_manager:
                    response2 = requests.get(url_get_songs + "?" + request.query_string.decode(), data=body,
                                             headers=request.headers)

                else:
                    print(f"User don't have admin right")
                return response2.json()
            else:
                return make_response(jsonify(
                    {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}), 401)
    elif request.method == 'POST':  # creez
        data = request.get_json()
        user = data['username']
        if 'Authorization' in request.headers:
            jwt = request.headers['Authorization']
            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)
            try:
                rep1 = parsare_post_validare(response.text)
            except:
                print("Nu ai token")
                return make_response(
                    jsonify({'message': 'Neautorizat-nu ai token'}),
                    403)
            roles = string_to_list(rep1)
            drept = autorizare_content_manager(roles)
            dreapt2 = autorizare_artist(roles)

            if drept or (parsare_post_validare_update(response.text) == user) and dreapt2:
                response2 = requests.post(url_post_songs, data=json.dumps(request.get_json()), headers=request.headers)
            else:
                print(f"User don't have admin right")
            return response2.json()
        else:
            return make_response(jsonify(
                {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}), 401)
    elif request.method == 'DELETE':
        data = request.get_json()
        user = data['username']
        if 'Authorization' in request.headers:
            jwt = request.headers['Authorization']
            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)
            try:
                rep1 = parsare_post_validare(response.text)
            except:
                print("Nu ai token")
                return make_response(
                    jsonify({'message': 'Neautorizat-nu ai token'}),
                    403)
            roles = string_to_list(rep1)
            drept = autorizare_content_manager(roles)
            dreapt2 = autorizare_artist(roles)

            if drept or (parsare_post_validare_update(response.text) == user) and dreapt2:
                response2 = requests.delete(url_delete_songs, data=json.dumps(request.get_json()),
                                            headers=request.headers)
            else:
                print(f"User don't have admin right")
            return response2.text
        else:
            return make_response(jsonify(
                {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}), 401)
    elif request.method == 'PUT':
        pass


@app.route('/playlists/', methods=['GET', 'POST', 'DELETE', 'PUT'])
def playlist():
    url = 'http://localhost:8000'
    if request.method == 'GET':  # owner pe playlist
        if len(request.query_string) == 0:  # getclasic
            if 'Authorization' in request.headers:
                jwt = request.headers['Authorization']
                body, headers = creare_cerere_validare(jwt)
                response = requests.post(url, data=body, headers=headers)
                try:
                    rep1 = parsare_post_validare(response.text)
                except:
                    print("Nu ai token")
                    return make_response(
                        jsonify({'message': 'Neautorizat-nu ai token'}),
                        403)
                roles = string_to_list(rep1)
                drept_client = autorizare_client(roles)
                if drept_client:
                    response2 = requests.get(url_get_playlist, data=body, headers=request.headers)
                else:
                    print(f"User don't have client right")
                return response2.json()
            else:
                return make_response(jsonify(
                    {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}), 401)
    elif request.method == 'POST':  # creez
        data = request.get_json()
        user = data['username']
        jwt = request.headers['Authorization']
        if 'Authorization' in request.headers:
            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)
            try:
                rep1 = parsare_post_validare(response.text)
            except:
                print("Nu ai token")
                return make_response(
                    jsonify({'message': 'Neautorizat-nu ai token'}),
                    403)
            roles = string_to_list(rep1)
            dreapt2 = autorizare_client(roles)

            if dreapt2 and (parsare_post_validare_update(response.text) == user):
                response2 = requests.post(url_post_playlist, data=json.dumps(request.get_json()),
                                          headers=request.headers)
            else:
                print(f"User don't have admin right")
            return response2.json()
        else:
            return make_response(jsonify(
                {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}), 401)
    elif request.method == 'DELETE':
        data = request.get_json()
        user = data['username']
        if 'Authorization' in request.headers:
            jwt = request.headers['Authorization']
            body, headers = creare_cerere_validare(jwt)
            response = requests.post(url, data=body, headers=headers)
            try:
                rep1 = parsare_post_validare(response.text)
            except:
                print("Nu ai token")
                return make_response(
                    jsonify({'message': 'Neautorizat-nu ai token'}),
                    403)
            roles = string_to_list(rep1)
            dreapt2 = autorizare_client(roles)

            if (parsare_post_validare_update(response.text) == user) and dreapt2:
                response2 = requests.delete(url_delete_playlist, data=json.dumps(request.get_json()),
                                            headers=request.headers)
            else:
                print(f"User don't have admin right")
            return response2.text
        else:
            return make_response(jsonify(
                {'message': 'Autentificarea nu a fost furnizata. Adaugati un token JWT in headerul cererii.'}), 401)
    elif request.method == 'PUT':
        pass


if __name__ == '__main__':
    app.run(port=8002)
