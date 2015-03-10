TShock Manager for Android
=============
Initial coding by Loganizer and Koesie10.
Updates by Loganizer

TShock Manager is a TShock server manager for Android that communicates to the server via the REST API.

## Setting up TShock ##

To enable the REST API in TShock and thus make your server ready to use with TShock Manager, you only have to enable one setting in the TShock configuration. Change `RestApiEnabled` to `true`. Make sure you open the port in `RestApiPort` so you can access it from your phone.

Your server is now ready for usage with TShock Manager. Open TShock Manager and add the server. When you click the server you should be able to use all features of the app :).

## Building TShockManager ##
TShockManager uses gradle to build the application. Build TShockManager by executing the following command:
```
gradlew build
```

## Contributing ##
When you start working on something, create an issue first so that we know that someone is working on it. When you have finished making changes, create a pull request and we will review the changes.


### Adding a new command ###
To add a new command, first add it to `co.tshock.manager.events.EventType` as a new field. Also add the params in a `EventParamInfo` object if needed. The next thing you should do is adding a method in `co.tshock.manager.api.TShockApi`. See the other methods as a reference. The last thing you have to do is add the command to the section in the UI. This can be done in one of the classes of `co.tshock.manager.ui.fragments.dashboard`. See `ServerFragment` for an example.

## To do ##

* Actually the project-libraries is not required anymore as it is only needed for Eclipse projects. But I left it here so Eclipse users don't get all kinds of errors.
* Create tests
* Create all fragments with all REST commands that exist (see [this](https://github.com/NyxStudios/TShock/blob/master/TShockAPI/Rest/RestManager.cs))
* Read all the TODO's in the code :)

### Bugs ###

* None I know of yet

### Features ###

* Real-time server chat


## Responses ##
### /v2/players/list ###
```
{
  "status": "200",
  "players": [
    {
      "nickname": "Koen",
      "username": "Koen",
      "group": "superadmin",
      "active": true,
      "state": 10,
      "team": 0
    },
    {
      "nickname": "Rinse",
      "username": "Rinse",
      "group": "guest",
      "active": true,
      "state": 10,
      "team": 0
    }
  ]
}
```

### /v2/users/read ###
```
{
    "status": "200",
    "group": "guest",
    "id": "3",
    "name": "Rinse"
}
```

### /v2/users/activelist ###
```
{
  "status": "200",
  "activeusers": "Koen\tRinse"
}
```