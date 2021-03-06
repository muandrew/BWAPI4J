////////////////////////////////////////////////////////////////////////////////
//
//    Copyright (C) 2017-2018 OpenBW Team
//
//    This file is part of BWAPI4J.
//
//    BWAPI4J is free software: you can redistribute it and/or modify
//    it under the terms of the Lesser GNU General Public License as published
//    by the Free Software Foundation, version 3 only.
//
//    BWAPI4J is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with BWAPI4J.  If not, see <http://www.gnu.org/licenses/>.
//
////////////////////////////////////////////////////////////////////////////////

#pragma once

#ifdef BWAPI4JBRIDGE_ENABLE_LOGGER

#include <iostream>
#include <memory>
#include <string>

#include <spdlog/spdlog.h>

namespace BWAPI4JBridge {
class Logger {
 public:
  Logger(const std::string &filename);

  auto get() { return _logger; }

 private:
  std::shared_ptr<spdlog::logger> _logger;
};

extern Logger logger;
}  // namespace BWAPI4JBridge

#if defined(_WIN32) || defined(WIN32)
#define __FILENAME__ (strrchr(__FILE__, '\\') ? strrchr(__FILE__, '\\') + 1 : __FILE__)
#else
#define __FILENAME__ (strrchr(__FILE__, '/') ? strrchr(__FILE__, '/') + 1 : __FILE__)
#endif

#define LOGGER(str)                                                                                   \
  {                                                                                                   \
    std::cout << __FILENAME__ << ":" << __FUNCTION__ << ":" << __LINE__ << " - " << str << std::endl; \
    BWAPI4JBridge::logger.get()->debug("{}:{}:{} - {}", __FILENAME__, __FUNCTION__, __LINE__, str);   \
    BWAPI4JBridge::logger.get()->flush();                                                             \
  }

#else
#define LOGGER(str) \
  {}
#endif
