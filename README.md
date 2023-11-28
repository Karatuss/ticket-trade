<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/TwoPair/ticket-trade">
    <img src="images/logo.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Ticket Trade</h3>

  <p align="center">
    Anybody can create a ticket trade for his/her own event with preventing from scalped tickets.
    <br />
    <a href="https://github.com/TwoPair/ticket-trade"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <!-- 나중에 데모 사이트 같은거 만들어서 보여주는 것도 나쁘지 않아보임. 링크 수정도 필요함. -->
    <a href="https://github.com/TwoPair/ticket-trade">View Demo</a>
    ·
    <a href="https://github.com/TwoPair/ticket-trade/issues">Report Bug</a>
    ·
    <a href="https://github.com/TwoPair/ticket-trade/issues">Request Feature</a>
  </p>




<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

These days, concerts or big events opend by famous celebrities have worried about scalpers.

The users who try to catch a ticket get into online ticketing service, such as Interpark Ticket, may experience that they were not able to get a ticket and found some scalped tickets which were illegally gathered with macro programs.

The problem is they cannot get a scalped ticket even if they pay for it. It is a scam, but there is no law to protect them.

<br>

So, **we thought we need to make a ticketing service with prevent users who use our service from scalpers at most before the law creates**.


### Built With

* [Hyperledger Fabric](https://github.com/hyperledger/fabric)



<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### ***🚨 Alert!! This project is working only on MAC environment!! 🚨***

### Prerequisites

Program versions which maintainers' use.
- Hyperledger fabric 2.5.4
- Docker Desktop 4.23.0
- Spring boot 3.1.4
- Java 17

1. Download Java 17 or higher.

2. Install prerequisites with following the link below.

[https://hyperledger-fabric.readthedocs.io/en/release-2.5/prereqs.html](https://hyperledger-fabric.readthedocs.io/en/release-2.5/prereqs.html)

3. Install the required fabric docker images, and binary.

[https://hyperledger-fabric.readthedocs.io/en/release-2.5/install.html](https://hyperledger-fabric.readthedocs.io/en/release-2.5/install.html)

When you run `./install-fabric.sh`, please specify the version of fabric 2.5.4.

```sh
./install-fabric.sh --fabric-version 2.5.4 docker
```
<!-- ./install-fabric.sh --fabric-version 2.5.4 docker binary -->

4. CouchBase setting for backend.

   - Docker (run the command below on shell)
     ```bash
     docker run -d --name db -p 8091-8094:8091-8094 -p 11210:11210 couchbase:community-6.5.0
     ```
    
   - IntelliJ IDEA
     - Run > Edit Configurations > Modify Options > ☑️ Add VM options 
     - INPUT
       ```
       -Dcom.sun.management.jmxremote.port=9000 
       -Dcom.sun.management.jmxremote.authenticate=false 
       -Dcom.sun.management.jmxremote.ssl=false
       ```
       <img width="1152" alt="Screenshot 2023-11-28 at 6 09 20 PM" src="https://github.com/TwoPair/ticket-trade/assets/39588815/8461a528-af9f-4026-a7cf-8522816366d9">
       <img width="456" alt="Screenshot 2023-11-28 at 6 10 10 PM" src="https://github.com/TwoPair/ticket-trade/assets/39588815/8f4ef876-a2b1-40cc-a006-e8c34fe4ec6a">
       <img width="703" alt="Screenshot 2023-11-28 at 6 11 07 PM" src="https://github.com/TwoPair/ticket-trade/assets/39588815/6838cafa-54c7-46d8-a850-9bc832225cd0">


### Clone & Run

1. Clone the ticket-trade where you want to work in.
   ```sh
   git clone https://github.com/TwoPair/ticket-trade.git
   ```
2. Run the following command to remove any containers or artifacts from any previous runs.
   ```sh
   cd blockchain-network/test-network
   ./network.sh down
   ./launcher.sh
   ```
   
3. Open `ticket-trade/Ticketing` folder with **IntelliJ**, selecet `Ticketing.java` file, and let's run it.


<!-- USAGE EXAMPLES -->
## Usage

1. User
   - If you login normally, you can face a event selection page.
   - Check and get inside a event you want to reserve, select seats and click the reservation button.
   - You can see your reservation in the event.

2. Manager
   - The admin gives you this accout when you want to create your own event.
   - After login with this accout, you can make an event and set options.
   - If users book your event, you can check who reserves.



<!-- ROADMAP -->
## Roadmap

See the [open issues](https://github.com/TwoPair/ticket-trade/issues) for a list of proposed features (and known issues).



<!-- CONTRIBUTING -->
<!-- 꼭 필요할까 -->
## Contributing

Contributions are what make the open source community such an amazing place to be learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE` for more information.



<!-- CONTACT -->
## Contact

Hyunmin Shin - shm1193@gmail.com
Youngrae Kim - kyrae604@naver.com
Sooeung Im   - dlatndmd7@naver.com

Project Link: [https://github.com/TwoPair/ticket-trade](https://github.com/TwoPair/ticket-trade)



<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/TwoPair/ticket-trade.svg?style=for-the-badge
[contributors-url]: https://github.com/TwoPair/ticket-trade/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/TwoPair/ticket-trade.svg?style=for-the-badge
[forks-url]: https://github.com/TwoPair/ticket-trade/network/members
[stars-shield]: https://img.shields.io/github/stars/TwoPair/ticket-trade.svg?style=for-the-badge
[stars-url]: https://github.com/TwoPair/ticket-trade/stargazers
[issues-shield]: https://img.shields.io/github/issues/TwoPair/ticket-trade.svg?style=for-the-badge
[issues-url]: https://github.com/TwoPair/ticket-trade/issues
[license-shield]: https://img.shields.io/github/license/TwoPair/ticket-trade.svg?style=for-the-badge
[license-url]: https://github.com/TwoPair/ticket-trade/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/TwoPair
