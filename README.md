## Practical computer science work : heat conduction through lumber.

Members : V.E, LB.

This project aims to model heat transfer through dry wood depending on :

1. the type of lumber : either decidious trees (balsa wood, oak, poplar), or softwood trees (spruce, tamarack) ;
2. the time ;
3. the depth of the wood sample.

This problem is simplified as a 1-D problem.

Let's suppose that a lumber sample can be modelled as a discontinuous line.

A heat source is located at x=0.

The reference heat flow is 4 kW.m-2.

We study the heat transfer through the wood at different times and temperatures.

Wood combustion can be seperated into three stages : the drying (T<200°C), the pyrolysis (200°C < T < 350°C) and the oxidation (350°C < T).

Since this project focuses on the drying of wood, we only consider temperatures which doens't exceed 200°C.

The problem is solved using the heat equation in 1D, Stefan-Boltzmann's law, Newton's law for convection, and Fourier's law for conduction.
