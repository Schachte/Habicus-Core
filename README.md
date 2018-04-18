

<p align="center">
		<img src="https://i.imgur.com/XxXZkmO.png"/>
</p>

-------
<div>
	<div style="display: inline-block">
		<img src="https://img.shields.io/badge/Habicus-Core-ff69b4.svg" />
	</div>
	<div style="display: inline-block">
		<img src="https://img.shields.io/badge/License-GPL-orange.svg" />
	</div>
</div>


Habicus is a platform for people who have a desire to reach their goals. Habicus is a data-proven accountability system that utilizes concepts from the fields of behavioral economics and beahvioral psychology to help people realize their full potential. Unlike existing tools, Habicus offers peer-to-peer and self-accountability options and does so in an easier to manage fashion.


## Installation

Windows, OS X, Linux
(In-Progress)

## Usage example

Are you tired of writing down goals only to fail days or weeks later? Habitate is a P2P accountability tool that puts financials on the line to ensure you achieve your goals at the interval you desire. Need an extra boost? Habitate enables you to add an additional verifier to your account so they can mark off that your goal for that day was completed in time. Failing to do so, the accountability user will receive the funds you pledged to that goal. 

## Development setup

In order to get the server running, run the following command:
```
gradle -Dspring.profiles.active=development bootRun
```

Profiles are currently setup in the application property files in the `resources` directory

## Test Database

The test database runs an embedded H2 database that is dropped on every rebuild. 

You can view the test database at `localhost:7777/h2` and login with the default credentials specified in the application-development.properties file

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

