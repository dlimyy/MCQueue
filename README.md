# MCQueue

## Motivation
Ever walked into a clinic with a fever and realising there are 15 people still in front of you? Don’t you wish that there’s a way to know in advance how much longer to wait? 
We have noticed that this is an increasingly common problem in SIngapore, especially in the Covid-19 Era, where there is an increasing crowding issue in clinics, from the rise in patients seeking medical attention,. This is especially important, given the high risks of crowding in closed medical environments during this pandemic.
The worst thing that could happen is that the clinic has such a long queue that when you arrive there,  you have no choice but to find an alternative clinic, but you don't know where to find the nearest clinic with the shortest queue!
In addition, when visiting the GP clinic, it is sometimes hard to find out whether your family doctor is in for the day as they might be working at other clinics. 
Additionally, did you realise it is troublesome to make an appointment at your clinic? For example, many clinics still employ walk-in-only appointment registration, which can be an inconvenience, and even inefficient.  
## Aim
We wish to be able to aid in decongesting clinics by digitising the current queue system so that patients will be aware of the current queue system at the various clinics. We wish to take an integrated approach, where patients can not only see the queue situation at a particular clinic, but any clinic in our network, to facilitate them to be able to find alternative clinics, helping to improve the efficiency of the healthcare system. This can also help reduce the patient load (redirecting patient flow) at overly-stretched clinics, and help reduce their workload.

Moreover, we would wish to provide patients with information regarding their doctor’s working hours and current work location (doctors will “check in” in our app) through our app “MCQueue”, which provides them ease in seeking medical attention from their family doctor, a goal we wish to align with the government’s goal of family doctors being the first line of care. 

Lastly, we will also seek to integrate a booking system into the app, such that patients can book medical appointments with the clinics islandwide, anytime, anywhere. This will greatly boost the efficiency of appointment-registrations, which is still largely manual at this point. This can not only provide convenience to the patients, but also help clinics better anticipate and prepare for the incoming 


## Features

**Real-Time Queue Situation/Status** 
* Allows users to select the clinics in our network and view the status at the selected clinic 
* Clinic network is visualised in a form of a map, such that users can visibly see the nearest alternative clinics 
* Filters a list of clinics within the close proximity of your current location

**Family Doctors Locator** 
* Allows patients to find out whether their doctor is currently around at the clinic 
* Patients can also find out the current clinic that the doctor is working at

**Online Booking System** 
* Users will be able to make appointments with clinics anytime, anywhere, in advance, which improves the convenience for patients,

**Notification system**
* Alerts patients when their turn is approaching (when they are 3 patients away from their turn) 
* Users will receive automated reminders on their appointment 
* Users will also receive notifications to inform them on updates in the queue status leading up to their appointment, such that they need not be at the clinic physically/have the app open

\
**Providing Insights on patient traffic with Data Analytics:**

Patient traffic and density data are stored in our system within a data warehouse. This data is used for Online Analytical Processing (OLAP). 

**Descriptive Analytics:** 
Patient cases can be categorised and formed into a visualisation dashboard for clinics to understand the core demographic of their patients. These insights can help clinics to further enhance their value offerings and tailor their services to their patients. For example, a clinic who sees a higher proportion of elderly patients can seek to enhance their staff’s proficiency in dialects to improve communication.

**Predictive Analytics:** 
Patient traffic and density data can be used by clinics to gain future insights for the clinic, which allows clinics to further optimise their manpower allocation strategies, to enhance the quality of their services to patients, as well as reduce unnecessary costs. The use of linear regression models can, for instance, highlight the low patient load on Wednesday afternoons. This can prompt clinics to allocate excess manpower from these periods to high-peak periods such as Monday mornings. This not only helps patients to get access to medical care faster, but also allows clinics to increase their operational efficiency.

## Acknowledgements
Icons made by Freepik from www.flaticon.com

Icons made by DinosoftLabs from www.flaticon.com
