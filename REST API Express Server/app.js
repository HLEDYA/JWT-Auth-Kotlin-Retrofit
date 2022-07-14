const express = require("express");
const app = express();
const jwt = require("jsonwebtoken");
const bodyParser = require("body-parser");

const accessTokenSecret = "secret-key";

const users = [
  {
    email: "yusuf@gmail.com",
    password: "qwerty", // hash password for production environments
    authToken: "none",
  },
  {
    email: "engin@gmail.com",
    password: "12345",
    authToken: "none",
  },
];

const books = [
  {
    title: "Harry Potter and The Sorcerer's Stone",
    author: "J.K. Rowling",
    pages: 309,
    year: 1998,
  },
  {
    title: "Dragons of Eden",
    author: "Carl Sagan",
    pages: 288,
    year: 1977,
  },
  {
    title: "The Dangers of Intelligence",
    author: "Isaac Asimov",
    pages: 216,
    year: 1986,
  },
];

app.use(bodyParser.json());
app.listen(3000, () => {
  console.log("Authentication service started on port 3000");
});

app.post("/login", (req, res) => {
  const { email, password } = req.body;
  console.log("email:" + email + " password:" + password);

  // This user is a pointer for the found user on the list
  // Not a new copy is created.
  const user = users.find((u) => {
    return u.email === email && u.password === password;
  });

  if (user) {
    console.log("login: user found. email: " + user.email);
    const authToken = jwt.sign({ email: user.email }, accessTokenSecret);
    // Following line modifies the user at the list,
    // Remember not a new copy of user was created above.
    user.authToken = authToken;

    res.json({
      authToken,
    });
  } else {
    res.send("email or password incorrect");
  }
});

const authenticateJWT = (req, res, next) => {
  const authHeader = req.headers.authorization;

  users.forEach(function (entry) {
    console.log(entry);
  });

  if (authHeader) {
    const token = authHeader.split(" ")[1];

    jwt.verify(token, accessTokenSecret, (err, user) => {
      if (err) {
        console.log("Token cannot be verified. err: " + err);
        return res.sendStatus(403);
      }
      const foundUser = users.find((u) => {
        return u.email === user.email;
      });

      if (foundUser == undefined) {
        console.log("This user does not exist in our user list: " + user.email);
        return res.sendStatus(403);
      }

      if (foundUser.authToken === "none") {
        console.log("This user does not have an authToken. Login required!");
        return res.sendStatus(403);
      }
      if (foundUser.authToken !== token) {
        console.log(
          "Token in req is not the same as the last created token for this user.\ntoken:" +
            token +
            "\nauthToken:" +
            foundUser.authToken
        );
        return res.sendStatus(403);
      }

      console.log("token verified for user: " + user.email);
      req.user = foundUser;
      next();
    });
  } else {
    console.log("no auth header found!");
    res.sendStatus(401);
  }
};

app.get("/books", authenticateJWT, (req, res) => {
  console.log("books info send to user: " + req.user.email);
  res.json(books);
});

app.post("/logout", authenticateJWT, (req, res) => {
  req.user.authToken = "none";
  res.send("Logout successful!");
});
