# Histogram

A runelite plugin that provides a visual window of gametick timings compared with various input events, factoring in inconsistent tickrates, ping, and server load. Intended to train prayer flicks, gear switching, movement, etc. But it's also useful for detecting server/networking issues.

Setup to detect inputs for attacking, eating, equipping items, moving, toggling prayers, toggling special attack, and using items.

The config includes several other options as well, like visualizing the effects of ping and/or server processing time, or visualizing the ideal tickrate.

Disclaimer: we can't access the exact times that the server processes our inputs. We can only approximate it based on ping and playercount. Occasionally an input will be processed before a tick but appear on the histogram after, or vice versa, but in my testing it's accurate more often than not.

If the timings are off for you for whatever reason, in the advanced section of the config you can customize the approximated delay yourself. Default values were found experimentally and may not be the same for everyone or for every specific type of input (ie. some 'use' actions might get handled by the server before others and have less delay, for example).
