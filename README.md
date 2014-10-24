ImgMap-BufferOverflow
=====================

"It's like losing pixels, but this time, you're gaining too much!" - Some senseless quote.

ImgMap is a Bukkit plugin that enables server owners, admins, moderators, and the like, to draw images on to the Minecraft map item.
Note that this plugin does not require the generation of a world in order to do so.

How does it work?
=================

In laymen's term:
Bukkit provides the ability to; truely a swell little thing that wasn't popularized until md_5 had mentioned it.

Technical:
Item maps are not rendered by the client; the server sends off 131 bytes (though, now of 1.8, this is *up* to 131, as compression was added) in the layout of:
Item Map ID (1byte) -> Scale -> (1byte) -> The x column we're editing (1byte) -> Map data (128bytes)
Using this, we can draw images on maps. Bukkit provides a nice API that allows us to do this easily (via Bukkit.getMap(short id) and creating an image renderer).
However, this is not true about videos/gif. In order to "stream" videos/animated gif images, I create all the packets required for the animation to display ahead of time.

Want to contribute?
===================

Sure, go ahead! Just submit a pull request and I'll (or someone else) review it.
Note that I'm not particularly nitpicky on your coding style. Unfortunately, do expect your code to lose its formatting or style.
Typically, the default Java guidelines/code style/standards are fine.
#####If you're interested in some basic guidelines:
 * I'm more of a K&R writer, however, I place any curly brackets for functions, methods, class declartions, etc. on the same line.
 * Remove any spaces between any method declarations and its opening curly bracket. Exception: if it throws an exception. Leave the space.
 * Remove any spaces in for-loops; particularly outside the loop. Preserve any spacing within the for-statement.
 * lowerCamelCase is love.
 * Be a little more verbose than normal. e.g "args" -> "arguments".

If you've managed to follow my absolutely unreasonable convention, congratulations. No offense, but you probably just wasted time doing that.

Donations?
==========

Currently not accepting monetary donations. However, if you want, you can gift me soemthing on [Steam](http://steamcommunity.com/id/childofheaven/).
