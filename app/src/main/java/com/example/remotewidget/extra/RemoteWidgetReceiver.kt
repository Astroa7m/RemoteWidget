package com.example.remotewidget.extra

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.example.remotewidget.presentation.component.RemoteWidget

class RemoteWidgetReceiver: GlanceAppWidgetReceiver()  {
    override val glanceAppWidget: GlanceAppWidget
        get() = RemoteWidget()
}