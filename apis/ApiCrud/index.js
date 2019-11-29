/*
author: Madroman.
*/


var uuid = require('uuid');
const express = require('express');
var mysql = require('mysql');
const bodyParser= require('body-parser');
const crypto = require('crypto');

app= express();

//cod for connect the data base sql to the proyect 
var con = mysql.createConnection({
    host: 'localhost',
    user: 'root',
    password:'',
    database:'chat'
});

con.connect( function(err){
    if(err)
        console.log(err);
    else
        console.log('la base de datos esta conectad');
});


//cod for ecrypting password
var generateRandomString = function(length){
    return crypto.randomBytes(Math.ceil(length/2)).toString('hex').slice(0,length);
};

var sha512 = function(password, salt){
    var hash = crypto.createHmac ('sha512', salt);
    hash.update (password);
    var value = hash.digest('hex');
    return {
        salt: salt,
        passwordHash: value
    };
};

function saltHasPassword(userPassword){
    var salt = generateRandomString(16); // generar contraseña encriptada con 16 caracteres de logitud
    var passwordData = sha512(userPassword, salt);

    return passwordData;
};

function checkHashPassword(user_password,salt){
    var passwordData= sha512(user_password, salt);
    return passwordData;
}





//>>>>>>>>>>>Cod for running web service

app.set('port', process.env.PORT || 3000);
 
app.use(bodyParser.json()); //incluir el uso de bodyparser mediante archivos json.
app.use(bodyParser.urlencoded({extended: true})); //aceptar parametros mediante url


//routing 
/*app.get("/", (req, res, next)=>{
    console.log('123');
   var encrypt = saltHasPassword("123");

    console.log('Password encrypted : ' + encrypt.passwordHash);
    console.log('salt '+encrypt.salt);
});*/



app.post('/register/',(req,res,next) =>{
    var post_data = req.body; // Obtener los valores mediante post
    var uid = uuid.v4(); //uede ser usado con un identificador específico "intencionalmente" y ser usado en varias ocasiones para identificar el mismo objeto en diferentes contextos
    var plaint_password = post_data.password; // optener la contraseña desde las peticiones body-parser.
    var hash_data = saltHasPassword(plaint_password);
    var password = hash_data.passwordHash;
    var salt = hash_data.salt;
    var name = post_data.name;
    var email = post_data.email;
    
    
    
    con.query('SELECT * FROM user WHERE email = ?',[email], function(err, result, fields){
        // si hay un erro en la conexion
      
        con.on('error', function(err){
            console.log('[MySQL ERROR]', err);
        });
        // se no se valida si ya existe el user
        if(result.length>0)
            //res.json('Esta direccion email ya esta registrada');
            res.status(400).send();
            //en caso de que no exista se lo inserta.
            else{
                con.query('INSERT INTO `user`( `UnqueId`, `Name`, `EncryptedPasword`, `Email`, `Salt`, `DateCreate`, `Update_at`) VALUES (?,?,?,?,?,NOW(),NOW())',[uid, name, password, email, salt], function(err, result, fields){
                //en caso de que alla un erro en la insercion se lo captura con: 
                con.on('error', function(err){
                    console.log('[MySQL ERROR]', err);
                    res.json('Error en el proceso de registro '+ err);
                    
                });

                //si no entro al anterior if, pues quiere decir que se registro satisfactoriamente. 
                //res.json('usuario registrado satisfactoriamente.')
                
                
                res.status(200).send(JSON.stringify(name))
                
                

            });
        }

    });
    
});

app.post('/login/', (req,res,next) =>{
    var post_data = req.body;
    var user_password = post_data.password;
    var email= post_data.email;


    con.query('SELECT * FROM user WHERE email = ?',[email], function(err, result, fields){
        // si hay un erro en la conexion
        con.on('error', function(err){
            console.log('[MySQL ERROR]', err);
        });
        if(result && result.length){
            
            var salt = result[0].Salt;
            var EncryptedPasword= result[0].EncryptedPasword;
            var hashPassword = checkHashPassword(user_password, salt).passwordHash;
            if(EncryptedPasword == hashPassword){
                //res.end(JSON.stringify(result[0]));

                const objToSend = {
                    name: result[0].Name,
                    email: result[0].Email
                }
                
                res.status(200).send(JSON.stringify(objToSend))
            }else{
                //res.end(JSON.stringify('Contraseña erronea'));
                res.status(404).send()
            }

        }
            
        else{ 
            
           res.status(404).send()
        }

    });
});

app.listen(app.get('port'),()=>{
    console.log('server on port', app.get('port'))
});

