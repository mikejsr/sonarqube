/*
 * SonarQube
 * Copyright (C) 2009-2019 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.scanner.repository;

import java.util.List;
import org.picocontainer.injectors.ProviderAdapter;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.sonar.api.utils.log.Profiler;
import org.sonar.scanner.bootstrap.ProcessedScannerProperties;
import org.sonar.scanner.rule.QualityProfiles;
import org.sonarqube.ws.Qualityprofiles.SearchWsResponse.QualityProfile;

public class QualityProfilesProvider extends ProviderAdapter {
  private static final Logger LOG = Loggers.get(QualityProfilesProvider.class);
  private static final String LOG_MSG = "Load quality profiles";
  private QualityProfiles profiles = null;

  public QualityProfiles provide(QualityProfileLoader loader, ProjectRepositories projectRepositories, ProcessedScannerProperties props) {
    if (this.profiles == null) {
      List<QualityProfile> profileList;
      Profiler profiler = Profiler.create(LOG).startInfo(LOG_MSG);
      if (!projectRepositories.exists()) {
        profileList = loader.loadDefault();
      } else {
        profileList = loader.load(props.getKeyWithBranch());
      }
      profiler.stopInfo();
      profiles = new QualityProfiles(profileList);
    }

    return profiles;
  }

}
