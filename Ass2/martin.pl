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
timeAndCost(_, Distance, Time, Cost, plane) :- Distance >= 400,
	Time is Distance / 500, Cost is Distance.
timeAndCost(_, Distance, Time, Cost, train) :- Time is Distance / 120, Cost is 0.75 * Distance.
timeAndCost(Town, Distance, Time, Cost, bus) :- Town \= a, Town \= e, Town \= f,
	Time is Distance / 80, Cost is 0.40 * Distance.
timeAndCost(_, Distance, Time, Cost, car) :- Time is Distance / 100, Cost is 0.60 * Distance.
timeAndCost(_, Distance, Time, Cost, walk) :- Time is Distance / 5, Cost is 0.

%Makes a trip from town A to town B while saving the visited towns (path) and the total distance.
trip(A, B, Path, [B | Path], Distance) :-
	route(A, B, Distance).
trip(A, B, PreviousPath, NewPath, Distance) :-
	route(A, C, D1), B \= C, \+(member(C, PreviousPath)), 
	trip(C, B, [C | PreviousPath], NewPath, D2), Distance is D1 + D2.

%Reverses a list. reverse([a, b, c], L): L = [c, b, a].
reverseList(List, ReversedList) :- reverseList(List, [], ReversedList).
reverseList([], ReversedList, ReversedList).
reverseList([H | T], Accumulator, ReversedList) :-
	reverseList(T, [H | Accumulator], ReversedList).

%Finds a path from town A to town B.
findPath(A, B, Path, Distance) :-
	trip(A, B, [A], ReversedPath, Distance), reverseList(ReversedPath, Path).

%Appends the second list to the first list.
append([], L, L).
append([H | T], L2, [H | L]) :- append(T, L2, L).

generate(S, Towns, Set, Distance, Time, Cost) :- findPath(S, S, Path, Distance), submember(Towns, Path), getTimeAndCost(Path, [], S1, 0, 0, Time, Cost),
	setof([S1, Distance, T1, C1], getTimeAndCost(Path, [], S1, 0, 0, T1, C1), Set).

generateCheapest(S, Towns, Solution, Distance, Time, Cost, MaxCost) :- generate(S, Towns, Set, Distance, Time, Cost), cheapest(Set, MaxCost, Solution).
cheapest([[Sol, Dis, Time, Cost] | _], MaxCost, [Sol, Dis, Time, Cost]) :- Cost < MaxCost, !.
cheapest([_ | Tail], MaxCost, Solution) :- cheapest(Tail, MaxCost, Solution).

generateFastest(S, Towns, Solution, Distance, Time, Cost, MaxTime) :- generate(S, Towns, Set, Distance, Time, Cost), fastest(Set, MaxTime, Solution).
fastest([[Sol, Dis, Time, Cost] | _], MaxTime, [Sol, Dis, Time, Cost]) :- Time < MaxTime, !.
fastest([_ | Tail], MaxTime, Solution) :- fastest(Tail, MaxTime, Solution).
	
getTimeAndCost([_ | []], Solution, Solution, Time, Cost, Time, Cost).
getTimeAndCost([T1, T2 | Tail], TempSolution, Solution, TempTime, TempCost, Time, Cost) :- route(T1, T2, Distance), timeAndCost(T1, Distance, Time1, Cost1, Mode),
	append(TempSolution, [T1, Mode], Sol2), append([T2], Tail, Path2), TempTime2 is TempTime + Time1, TempCost2 is TempCost + Cost1,
	getTimeAndCost(Path2, Sol2, Solution, TempTime2, TempCost2, Time, Cost).

submember([], _).
submember([H | T], L2) :- member(H, L2), !, submember(T, L2).

findFirst(S, Towns, Solution, Distance, Time, Cost) :- setof([Sol, D, T, C], generate(S, Towns, Sol, D, T, C), Set),
	findFirst(Set,[Solution, Distance, Time, Cost]).
findFirst([Solution | _], Solution).

%Prints [a, car, b] as 'a car b'.
print([]).
print([H | T]) :- write(H), write(' '), print(T).

%Question 1:
tripMaxTime(S, Towns, MaxTime) :- generateFastest(S, Towns, Solution, Distance, Time, Cost, MaxTime), Time < MaxTime, printTrip(Solution, Distance, Time, Cost).
q1 :- tripMaxTime(s, [a], 15).

%Question 2:
tripMaxCost(S, Towns, MaxCost) :- generateCheapest(S, Towns, Solution, Distance, Time, Cost, MaxCost), Cost < MaxCost, printTrip(Solution, Distance, Time, Cost).
q2 :- tripMaxCost(s, [a,b,c,d,e,f,g], 2000).

%Prints a path, its distance, time and cost.
printTrip([Path, _, _, _], Distance, Time, Cost) :- print(Path), write('s'), nl, write('Total distance: '), write(Distance), write('km.'),
	nl, write('Total time: '), write(Time), write('h.'), nl, write('Total cost: $'), write(Cost), write('.').


