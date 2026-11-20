# EV Charging Network

A menu-driven Java console application for booking electric-vehicle charging slots across Mumbai. Built as an Object-Oriented Programming course team project at NMIMS MPSTME.

**Team:** Prerak Jhala, [teammate name]

## What it does
- **Accounts:** sign up (name, email, password, phone, number plate, car type) and sign in with email and password.
- **Booking:** pick an area, then a station (20 stations across 5 areas, sorted by free chargers), then an AC or fast charger. Enter current and target battery %, and the app calculates energy, charging time, cost and 18% GST.
- **Payment:** UPI, card or cash. This is a simulation; no real money is involved. A bill is shown on screen and saved to `bills/`.
- **History:** last transaction, a date range, or all bookings, each exported as a CSV that opens in Excel (`reports/`).
- **Profile:** total bookings, energy charged and amount spent.

## Design
| OOP concept | Where |
|---|---|
| Inheritance | `User extends Person`; `ACCharger` and `FastCharger extend ChargingPoint` |
| Abstract class + runtime polymorphism | `ChargingPoint` with `calculateCost()` and `chargingTime()` overridden per charger type |
| Interface | `Payable`, implemented by `UpiPayment`, `CardPayment`, `CashPayment` |
| Custom checked exceptions | `InvalidLoginException`, `ChargerNotAvailableException` |
| File handling | `FileManager`: users, transactions, bills and CSV reports with buffered readers and writers |
| Collections, lambda, Comparator | `ArrayList<Station>` sorted with a lambda; history sorted with an anonymous `Comparator` |

**Cost model:** units = battery size × (target % − current %) / 100, with battery sizes of 30 / 40 / 60 kWh for hatchback / sedan / SUV. AC costs ₹12 per unit; fast costs ₹20 per unit + ₹50. GST is 18%.

## Run it
Needs Java 8 or newer. From this folder:
```
javac -d bin src/*.java
java -cp bin Main
```
On GitHub, open the repository in a Codespace (**Code → Codespaces → Create codespace**) and run the same two lines in the terminal.

**Demo account:** `demo@ev.com` / `demo123` (already has 8 past bookings, so the history and CSV export can be tried straight away).
