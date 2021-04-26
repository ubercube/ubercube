
Ubercube
===
![](https://travis-ci.org/ubercube/ubercube.svg?branch=develop)

Ubercube is a first person shooter multi-player game set in a completely destroyable voxel world.
The map can be procedurally generated or pre-made.

The game is in development, for more information on it we have french updates and more info on the [#Ubercube](https://twitter.com/hashtag/Ubercube?src=hash) and on the [Ubercube](https://twitter.com/UbercubeGame) twitter account.

Ubercube is fully open source and licensed under the [GNU 3.0 License](http://www.gnu.org/licenses/) and available in source code form at [GitHub](https://github.com/TeamUbercube/ubercube)

![Ubercube](https://cdn.pgmp.dev/photos/ubercube.png "Ubercube")

## The Team
- **[mploux/Marccspro](https://github.com/mploux)** - Lead Programmer
- **[Jimi Vacarians](https://github.com/jb-perrier)** - Programmer/Art
- **[Tybau](https://github.com/Tybau)** - Programmer
- **[Custom2043]()** - Programmer
- **[Arthur/Neko](https://twitter.com/ArthurBaurens)** - Programmer
- **[Nik'](https://twitter.com/NikGraph)** - Communication

## How to play ?
There are two different ways you can play the game:

You can compile the game yourself by following the steps described in the **How to compile** section, or
you can download the game directly and play either on are own servers for free or on your own custom servers at this link: https://ubercube.github.io/

Either ways, have fun !

![Ubercube](http://i.imgur.com/D7qmGQP.png "Ubercube")

## How to report a bug ?
If you encounter any bugs, you can submit an [issue](https://github.com/ubercube/ubercube/issues) and we will do are best to fix it. If you understand French you can also join are discord server here: https://discord.gg/0vBqM6uVbNfmsUkX

## How to compile ?

Clone master's branch (to compile the official ubercube release) on your computer:
```sh
$ git clone --depth=1 https://github.com/TeamUbercube/ubercube.git
```

Or download the archive [here](https://github.com/TeamUbercube/ubercube/archive/master.zip).

The compilation is done with **Maven** so you need to install it.
```sh
$ cd ubercube
$ mvn package assembly:single
```

Then you just have to run a **server** then a **client**.

#### Client :

```sh
$ java -cp ubercube.jar fr.veridiangames.client.MainComponent ip:port username
```
#### Server :

```sh
$ java -cp ubercube.jar fr.veridiangames.server.ServerMain port
```

## Credits
Thanks to them !
- **[MimusAngel](https://twitter.com/Mimus_Angel)** - for helping in the beginning with the game and for his awesome BufferData class !
- **[Arthur/Neko](https://twitter.com/ArthurBaurens)** - for helping with the game management and for his awesome help with the physics and for the launcher !
- **[Freezee](https://twitter.com/Freezee_Freeze)** - for helping with gameplay ideas and with game management !
- **[MrDev023](https://twitter.com/MrDev023)** - for helping with the launcher !
- **[Aiko](https://twitter.com/YanisAtl)** - for helping with the network stuff !

And thanks to everybody who gave ideas and everybody who helped with the developpement !
