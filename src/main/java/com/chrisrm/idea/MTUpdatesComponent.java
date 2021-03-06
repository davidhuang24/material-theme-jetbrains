/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Chris Magnussen and Elior Boukhobza
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 */

package com.chrisrm.idea;

import com.chrisrm.idea.utils.MTStatisticsNotification;
import com.chrisrm.idea.utils.Notify;
import com.intellij.ide.util.PropertiesComponent;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.event.HyperlinkEvent;

public final class MTUpdatesComponent extends AbstractProjectComponent {
  private MTApplicationComponent application;

  protected MTUpdatesComponent(final Project project) {
    super(project);
  }

  @Override
  public void initComponent() {
    application = MTApplicationComponent.getInstance();
  }

  @Override
  public void projectOpened() {
    if (application.isUpdated()) {
      Notify.showUpdate(myProject);
    }

    // Show agreement
    if (!application.isAgreementShown()) {
      final Notification notification = createStatsNotification(
          (notification1, event) -> {
            MTConfig.getInstance().setAllowDataCollection(event.getEventType().equals(HyperlinkEvent.EventType.ACTIVATED));
            PropertiesComponent.getInstance().setValue(MTApplicationComponent.SHOW_STATISTICS_AGREEMENT, true);
            notification1.expire();
          });

      Notifications.Bus.notify(notification, myProject);
    }
  }

  public Notification createStatsNotification(@Nullable final NotificationListener listener) {
    return new MTStatisticsNotification(listener);
  }

  @Override
  public void disposeComponent() {
    application = null;
  }

  @NotNull
  @Override
  public String getComponentName() {
    return "MTUpdatesComponent";
  }
}
