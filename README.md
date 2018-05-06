


<p align="center">
		<img src="https://i.imgur.com/XxXZkmO.png"/>
</p>

-------

[![License](https://img.shields.io/badge/Habicus-Core-ff69b4.svg)]()
[![License](https://img.shields.io/badge/License-GPL-orange.svg)]()
[![Build Status](https://travis-ci.org/Habicus/Habicus-Core-Web.svg?branch=master)](https://travis-ci.org/Habicus/Habicus-Core-Web)
[![Waffle.io - Columns and their card count](https://badge.waffle.io/Habicus/Habicus-Core-Web.png?columns=all)](https://waffle.io/Habicus/Habicus-Core-Web?utm_source=badge)

Habicus is a platform for people who have a desire to reach their goals. Habicus is a data-proven accountability system that utilizes concepts from the fields of behavioral economics and beahvioral psychology to help people realize their full potential. Unlike existing tools, Habicus offers peer-to-peer and self-accountability options and does so in an easier to manage fashion.


## Installation

Windows, OS X, Linux

Clone the repository:
```
git clone https://github.com/Habicus/Habicus-Core-Web habicus-core-web && cd habicus-core-web
```

- Setup a local running MySQL instance with the username of: `root`
- Set an environment variable under `MYSQL_PASSWORD` to be picked up by the development profile

Run the server: `gradle spotlessApply && gradle dev`. (See below for more advanced config)

## Background

Are you tired of writing down goals only to fail days or weeks later? Habicus is a P2P accountability tool that puts financials on the line to ensure you achieve your goals at the interval you desire. Need an extra boost? Habicus enables you to add an additional verifier to your account so they can mark off that your goal for that day was completed in time. Failing to do so, the accountability user will receive the funds you pledged to that goal. 

## Development Setup

In order to get the server running, run the following command:
```
gradle dev
```

To enable debug
```
gradle dev -Pdebug
```

All files must have the proper formatting + headers, if you get a verification failure
```
gradle spotlessApply
```

It is highly recommended to setup an alias for all development as follows: 
```
alias qb='gradle spotlessApply && gradle dev'
```

Profiles are currently setup in the application property files in the `resources` directory

## Test Database

The test database runs an embedded H2 database that is dropped on every rebuild. 

You can view the test database at `localhost:7777/db` and login with the default credentials specified in the application-development.properties file

## Release History

* 0.0.1
    * Work in progress

## Distribution & Licensing

Content is licensed under CC-BY-SA 3.0: 
http://creativecommons.org/licenses/by-sa/3.0/

Code is GNU GPL v3.  You can modify and contribute to the code.  You can deploy the code on your own servers; however, you cannot use your own deployed instances for commercial purposes.

Distributed under the GNU GPL v3 license. See ``LICENSE`` for more information.

## License 
[Click to View GNU GPL v3 License](https://www.gnu.org/licenses/gpl-3.0.en.html)

## Contributing

1. Fork it (<https://github.com/yourname/yourproject/fork>)
2. Create your feature branch (`git checkout -b feature/fooBar`)
3. Commit your changes (`git commit -am 'H-[TICKET NUMBER/ISSUE NUMBER] Add some fooBar'`)
4. Push to the branch (`git push origin feature/fooBar`)
5. Create a new Pull Request

