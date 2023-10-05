package com.histogram;

import net.runelite.client.ui.overlay.Overlay;
import java.awt.*;
import java.util.*;

class HistogramOverlay extends Overlay
{
    private class Event
    {
        public EventType type;
        public float time;
        public float inputOffset;
        public float serverOffset;

        Event(EventType type)
        {
            this.type = type;
            this.time = 0;
            this.inputOffset = 0;
            this.serverOffset = 0;
        }
    }

    private ArrayDeque<Event> events;

    private HistogramConfig config;

    private long lastnano;
    private float delta;

    HistogramOverlay(HistogramConfig config)
    {
        this.config = config;

        events = new ArrayDeque<>();
        lastnano = System.nanoTime();
    }

    public void addEvent(EventType type)
    {
        Event event = new Event(type);
        events.add(event);
    }

    public void addEvent(EventType type, float delay)
    {
        Event event = new Event(type);
        event.time -= delay;
        events.add(event);
    }

    public void addEvent(EventType type, float pingdelay, float serverdelay)
    {
        Event event = new Event(type);
        event.time -= pingdelay + serverdelay;
        event.serverOffset = serverdelay;
        event.inputOffset = pingdelay + serverdelay;
        events.add(event);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        graphics.setColor(config.bgColor());
        graphics.fillRect(0, 0, config.panelSize().width, config.panelSize().height);

        updateDelta();
        updateEvents();
        drawEvents(graphics);

        return config.panelSize();
    }

    private void drawEvents(Graphics2D graphics)
    {
        for (Event event : events)
        {
            drawBar(graphics, event.time, event.type);

            if (event.inputOffset != 0 && event.serverOffset != 0)
            {
                if (config.showInputLag())
                {
                    drawRange(graphics, event.time + event.inputOffset, event.time, event.type, config.inputLagAlpha());
                }

                if (config.showServerLag())
                {
                    drawRange(graphics, event.time + event.serverOffset, event.time, event.type, config.serverLagAlpha());
                }
            }
        }
    }

    private void drawBar(Graphics2D graphics, float time, EventType type)
    {
        Color color = typeToColor(type);
        float xpos = lerp(config.panelSize().width, 0, time / (config.durationMS() / 1000f));

        int fullbarwidth = config.linewidth() - ((config.antialiasing()) ? 1 : 0);
        int fullbaroffset = (int)xpos + ((config.antialiasing() ? 1 : 0));
        drawRect(graphics, color, fullbaroffset, fullbarwidth);

        if (config.antialiasing())
        {
            float subpixel = xpos % 1 + ((xpos < 0) ? 1 : 0);

            Color left = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * (1 - subpixel)));
            Color right = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * subpixel));

            drawRect(graphics, left, (int)xpos, 1);
            drawRect(graphics, right, (int)xpos + config.linewidth(), 1);
        }
    }

    private void drawRange(Graphics2D graphics, float start, float end, EventType type, int alpha)
    {
        Color originalColor = typeToColor(type);
        Color color = new Color(originalColor.getRed(), originalColor.getGreen(), originalColor.getBlue(), (int)(originalColor.getAlpha() * (alpha / 255f)));

        float xpos = lerp(config.panelSize().width, 0, start / (config.durationMS() / 1000f));
        float width = lerp(config.panelSize().width, 0, end / (config.durationMS() / 1000f)) - xpos;
        float subpixel = xpos % 1 + ((xpos < 0) ? 1 : 0);

        int fullbarwidth = (int)(width + subpixel + 1);
        int fullbaroffset = (int)xpos + ((config.antialiasing() ? 1 : 0));
        drawRect(graphics, color, fullbaroffset, fullbarwidth);

        if (config.antialiasing())
        {
            Color left = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int) (color.getAlpha() * (1 - subpixel)));
            drawRect(graphics, left, (int)xpos, 1);
        }
    }

    private void drawRect(Graphics2D graphics, Color color, int xpos, int width)
    {
        if (width <= 0)
            return;

        if (xpos + width < 0 || xpos >= config.panelSize().width)
            return;

        if (color.getAlpha() == 0)
            return;

        int boundedx = Math.max(xpos, 0);
        int boundedw = Math.min(xpos + width, config.panelSize().width) - boundedx;

        graphics.setColor(color);
        graphics.fillRect(boundedx, 0, boundedw, config.panelSize().height);
    }

    private Color typeToColor(EventType type)
    {
        switch (type)
        {
            case TICK:
                return config.tickColor();
            case IDEAL_TICK:
                return config.idealTickColor();
            case EQUIP:
                return config.equipColor();
            case EAT:
                return config.eatColor();
            case MOVE:
                return config.moveColor();
            case USE:
                return config.useColor();
            case ATTACK:
                return config.attackColor();
            case SPECIAL_ATTACK:
                return config.specColor();
            case PRAYER:
                return config.prayerColor();
            case CUSTOM_1:
                return config.custom1Color();
            case CUSTOM_2:
                return config.custom2Color();
            case CUSTOM_3:
                return config.custom3Color();
            case CUSTOM_4:
                return config.custom4Color();
            case CUSTOM_5:
                return config.custom5Color();
            default:
                return config.tickColor();
        }
    }

    private float lerp(float a, float b, float t)
    {
        return a * (1 - t) + b * t;
    }

    private void updateDelta()
    {
        long nano = System.nanoTime();
        delta = (nano - lastnano) / 1000000000f;
        lastnano = nano;
    }

    public void updateEvents()
    {
        for (Event event : events)
        {
            event.time += delta;
        }

        // events in the deque aren't strictly ordered by time, because of ping delays
        // this "assumes" that they are to strictly ordered to remove them as soon as possible
        // as it is some events will persist longer than they need to because of these delays
        // however, all events will eventually surpass the max duration, so it's not an issue
        // the maximum duration, if kept at sane levels, shouldn't ever result in much buildup
        float extratime = 1 + (float)config.linewidth() / (float)config.panelSize().width;
        while (!events.isEmpty() && events.peek().time > (config.durationMS() / 1000f * extratime))
        {
            events.removeFirst();
        }
    }
}
