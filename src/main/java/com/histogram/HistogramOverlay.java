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

        Event(EventType type)
        {
            this.type = type;
            this.time = 0;
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
        }
    }

    private void drawBar(Graphics2D graphics, float time, EventType type)
    {
        if (time >= config.durationMS() / 1000f || time < 0)
            return;

        Color color = typeToColor(type);
        float xpos = lerp(config.panelSize().width, 0, time / (config.durationMS() / 1000f));

        if (config.antialiasing())
        {
            float subpixel = xpos - ((int)xpos);
            Color left = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * (1 - subpixel)));
            Color right = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha() * subpixel));

            graphics.setColor(left);
            graphics.fillRect((int)xpos, 0, 1, config.panelSize().height);

            if ((int)xpos + 1 < config.panelSize().width)
            {
                graphics.setColor(right);
                graphics.fillRect((int) xpos + 1, 0, 1, config.panelSize().height);
            }
        }
        else
        {
            graphics.setColor(color);
            graphics.fillRect((int)xpos, 0, 1, config.panelSize().height);
        }
    }

    private Color typeToColor(EventType type)
    {
        switch (type)
        {
            case TICK:
                return config.tickColor();
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
            case PRAYER:
                return config.prayerColor();
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
        while (!events.isEmpty() && events.peek().time > (config.durationMS() / 1000f))
        {
            events.removeFirst();
        }
    }
}
