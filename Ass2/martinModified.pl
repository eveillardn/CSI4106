%Martin version - WIP

%edge(town1, town2, distance).
edge(s, a, 300).
edge(s, d, 20).
edge(a, b, 1500).
edge(a, d, 400).
edge(b, c, 9).
edge(b, e, 200).
edge(c, d, 2000).
edge(c, g, 12).
edge(d, e, 3).
edge(e, f, 400).
edge(f, g, 800).

%Since our edges are bidirectional, edge(A,B) means we can go from A to B and from B to A.
route(A, B, Distance) :- edge(A, B, Distance).
route(A, B, Distance) :- edge(B, A, Distance).

%Calculates the time and cost when travelling a Distance from Town.
answerTimeAndCost(Time,Cost,Time,Cost).
timeAndCost(_, Distance, Time, Cost, plane) :- Distance >= 400,
	Time2 is Distance / 500, Cost2 is Distance, answerTimeAndCost(Time,Cost,Time2,Cost2).
timeAndCost(_, Distance, Time, Cost, train) :- Time2 is Distance / 120, Cost2 is 0.75 * Distance, answerTimeAndCost(Time,Cost,Time2,Cost2).
timeAndCost(Town, Distance, Time, Cost, bus) :- Town \= a, Town \= e, Town \= f, Time2 is Distance / 80, Cost2 is 0.40 * Distance, answerTimeAndCost(Time,Cost,Time2,Cost2).
timeAndCost(_, Distance, Time, Cost, car) :- Time2 is Distance / 100, Cost2 is 0.60 * Distance, answerTimeAndCost(Time,Cost,Time2,Cost2).
timeAndCost(_, Distance, Time, Cost, walk) :- Time2 is Distance / 5, Cost2 is 0, answerTimeAndCost(Time,Cost,Time2,Cost2).

%Returns all possible Modes of transportations for this traveled Distance from given Town, along with their Cost, while sorted by Time
quickTravel(Town, Distance,Solution) :-
	setof([Time,Cost,Mode],timeAndCost(Town, Distance,Time,Cost,Mode),Solution).
%Only returns quickest possible movement
quickestTravel(Town, Distance,Time,Cost,Mode):- quickTravel(Town, Distance,[[Time,Cost,Mode]|_]).

%Returns all possible Modes of transportations for this traveled Distance from given Town, along with their Time, while sorted by Cost	
cheapTravel(Town, Distance,Solution) :-
	setof([Cost,Time,Mode],timeAndCost(Town, Distance,Time,Cost,Mode),Solution).
%Only returns cheapest possible movement
cheapestTravel(Town, Distance,Time,Cost,Mode):- cheapTravel(Town, Distance,[[Cost,Time,Mode]|_]).
	
%Returns a Solution displaying traveled nodes with the mode of transportation, the total Time of this solution, and the total Cost of this solution. Emphasis is on the speed and doesn't account for cost.
getQuickTimeAndCost([_ | []], Solution, Solution, Time, Cost, Time, Cost).
getQuickTimeAndCost([T1, T2 | Tail], TempSolution, Solution, TempTime, TempCost, Time, Cost) :- route(T1, T2, Distance), quickestTravel(T1, Distance, Time1, Cost1, Mode),
	append(TempSolution, [T1, Mode], Sol2), append([T2], Tail, Path2), TempTime2 is TempTime + Time1, TempCost2 is TempCost + Cost1,
	getQuickTimeAndCost(Path2, Sol2, Solution, TempTime2, TempCost2, Time, Cost).

%Returns a Solution displaying traveled nodes with the mode of transportation, the total Time of this solution, and the total Cost of this solution. Emphasis is on the cost and doesn't account for speed.	
getCheapTimeAndCost([_ | []], Solution, Solution, Time, Cost, Time, Cost).
getCheapTimeAndCost([T1, T2 |Tail], TempSolution, Solution, TempTime, TempCost, Time, Cost) :- route(T1, T2, Distance), cheapestTravel(T1, Distance, Time1, Cost1, Mode),
	append(TempSolution, [T1, Mode], Sol2), append([T2], Tail, Path2), TempTime2 is TempTime + Time1, TempCost2 is TempCost + Cost1,
	getCheapTimeAndCost(Path2, Sol2, Solution, TempTime2, TempCost2, Time, Cost).
	
%Generates a "Quick" solution from starting node S hitting at least all Towns specified exactly once and going back to s
generateQuick(S, Towns, Solution, Distance, Time, Cost) :- 
	setof([Path,Distance1],findPath(S, S, Path, Distance1,Towns),Set),
	allElemsCheckQuick(WithLenghts,[],Set),
	setof([Time1,S1,Cost1,Distance1],allElemsQuick(WithLenghts,Time1,S1,Cost1,Distance1),[[Time,Solution,Cost,Distance]|_]).

%Generates a "Cheap" solution from starting node S hitting at least all Towns specified exactly once and going back to s
generateCheap(S, Towns, Solution, Distance, Time, Cost) :- 
	setof([Path,Distance1],findPath(S, S, Path, Distance1,Towns),Set),
	allElemsCheckCheap(WithLenghts,[],Set),
	setof([Cost1,S1,Time1,Distance1],allElemsCheap(WithLenghts,Cost1,S1,Time1,Distance1),[[Cost,Solution,Time,Distance]|_]).
	
%Sorry about the horrible name, this method takes prebuild lists of the form [[Path, Distance], [Path, Distance],...] and adds Costs and Times to these paths
allElemsCheckCheap(Solution,Solution,[]).
allElemsCheckCheap(Solution,TempSolution,[[H,Distance]|T]):-
	getCheapTimeAndCost(H,[],S1,0,0,Time,Cost),
	append(TempSolution,[[Cost,S1,Time,Distance]],NewTemp),
	allElemsCheckCheap(Solution,NewTemp,T).

allElemsCheckQuick(Solution,Solution,[]).
allElemsCheckQuick(Solution,TempSolution,[[H,Distance]|T]):-
	getQuickTimeAndCost(H,[],S1,0,0,Time,Cost),
	append(TempSolution,[[Time,S1,Cost,Distance]],NewTemp),
	allElemsCheckQuick(Solution,NewTemp,T).
	
%Again, a horrible name and a very sketchy method. This method is used as a very lazy way to sort a list of lists containing many elements using the setof() command.
allElemsCheap([[Cost,Path,Time,Distance]|[]],Cost,Path,Time,Distance).	%Happens upon last element
allElemsCheap([[Cost,Path,Time,Distance]|_],Cost,Path,Time,Distance).	%Happens on every element but the last one
allElemsCheap([_|T],Cost,Path,Time,Distance):-							%Happens as long as the list isn't empty
	allElemsCheap(T,Cost,Path,Time,Distance).
	
allElemsQuick([[Time,Path,Cost,Distance]|[]],Time,Path,Cost,Distance).	%Happens upon last element
allElemsQuick([[Time,Path,Cost,Distance]|_],Time,Path,Cost,Distance).	%Happens on every element but the last one
allElemsQuick([_|T],Time,Path,Cost,Distance):-							%Happens as long as the list isn't empty
	allElemsQuick(T,Time,Path,Cost,Distance).
	
%Makes a trip from town A to town B while saving the visited towns (path) and the total distance.
trip(A, B, Path, [B | Path], Distance,Towns) :-
	route(A, B, Distance),
	subset(Towns,[B|Path]).
trip(A, B, PreviousPath, NewPath, Distance,Towns) :-
	route(A, C, D1), B \= C, \+(member(C, PreviousPath)), 
	trip(C, B, [C | PreviousPath], NewPath, D2, Towns), Distance is D1 + D2.

%Reverses a list. reverse([a, b, c], L): L = [c, b, a].
reverseList(List, ReversedList) :- reverseList(List, [], ReversedList).
reverseList([], ReversedList, ReversedList).
reverseList([H | T], Accumulator, ReversedList) :-
	reverseList(T, [H | Accumulator], ReversedList).

%Finds a path from town A to town B.
findPath(A, B, Path, Distance,Towns) :-
	trip(A, B, [A], ReversedPath, Distance,Towns), submember(Towns, ReversedPath), reverseList(ReversedPath, Path).

%Appends the second list to the first list.
append([], L, L).
append([H | T], L2, [H | L]) :- append(T, L2, L).

submember([], _).
submember([H | T], L2) :- member(H, L2), !, submember(T, L2).

%Prints [a, car, b] as 'a car b'.
print([]).
print([H | T]) :- write(H), write(' '), print(T).

%Question 1: 
q1:-
	setof([Solution,Distance,Time,Cost],generateQuick(s,[b,e,c],Solution,Distance,Time,Cost),[[S,D,T,C]|_]),
	Time @< 15,
	printTrip(S,D,T,C).

%Question 2:
q2:-
	setof([Solution,Distance,Time,Cost],generateCheap(s,[a,b,c,d,e,f,g],Solution,Distance,Time,Cost),[[S,D,T,C]|_]),
	Cost @< 2000,
	printTrip(S,D,T,C).

%Question 3:
q3:-
	setof([Solution,Distance,Time,Cost],generateQuick(s,[c],Solution,Distance,Time,Cost),[[S,D,T,C]|_]),
	printTrip(S,D,T,C).
	
%SanityTest This path should be at max either [s,a,d,s] or [s,d,a,s]
test1:-
	setof([Solution,Distance,Time,Cost],generateQuick(s,[a],Solution,Distance,Time,Cost),[[S,D,T,C]|_]),
	printTrip(S,D,T,C).

%Prints a path, its distance, time and cost.
printTrip(Path, Distance, Time, Cost) :- print(Path), write('s'), nl, write('Total distance: '), write(Distance), write('km.'),
	nl, write('Total time: '), write(Time), write('h.'), nl, write('Total cost: $'), write(Cost), write('.').