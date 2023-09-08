# Histogram

A runelite plugin that provides a visual window of gametick timings compared with various input events, factoring in inconsistent tickrates and ping. Intended to train prayer flicks, gear switching, movement, etc. But it's also useful for detecting server/networking issues.

Disclaimer: we can't access the exact times that the server processes our inputs. We can only approximate it based on ping. Occasionally an input will be processed before a tick but appear on the histogram after, or vice versa, but in my testing it's accurate more often than not.