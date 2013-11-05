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

%submember([Element, _], List) returns true if [Element, _] is a member of List.
submember(X, [[_, Head] | _]) :- X = Head, !.
submember(X, [_ | Tail]) :- submember(X, Tail).

%Calculates the time and cost when travelling a Distance from Town.
timeAndCost(_, Distance, Time, Cost, plane) :- Distance >= 400,
	Time is Distance / 500, Cost is Distance.
timeAndCost(_, Distance, Time, Cost, train) :- Time is Distance / 120, Cost is 0.75 * Distance.
timeAndCost(Town, Distance, Time, Cost, bus) :- Town \= a, Town \= e, Town \= f,
	Time is Distance / 80, Cost is 0.40 * Distance.
timeAndCost(_, Distance, Time, Cost, car) :- Time is Distance / 100, Cost is 0.60 * Distance.
timeAndCost(_, Distance, Time, Cost, walk) :- Time is Distance / 5, Cost is 0.

%Makes a trip from town A to town B while saving the visited towns (path) and the total distance.
trip(A, B, Path, [[Mode, B] | Path], Distance, Time, Cost) :-
	route(A, B, Distance), timeAndCost(A, Distance, Time, Cost, Mode).
trip(A, B, PreviousPath, NewPath, Distance, Time, Cost) :-
	route(A, C, D1), B \= C, \+(submember(C, PreviousPath)), timeAndCost(A, D1, T1, C1, M1),
	trip(C, B, [[M1, C] | PreviousPath], NewPath, D2, T2, C2), timeAndCost(C, D2, T2, C2, _),
	Distance is D1 + D2, Time is T1 + T2, Cost is C1 + C2.

%Reverses a list. reverse([a, b, c], L): L = [c, b, a].
reverseList(List, ReversedList) :- reverseList(List, [], ReversedList).
reverseList([], ReversedList, ReversedList).
reverseList([H | T], Accumulator, ReversedList) :-
	reverseList(T, [H | Accumulator], ReversedList).

%Finds a path from town A to town B.
findPath(A, B, Path, Distance, Time, Cost) :-
	trip(A, B, [[start, A]], ReversedPath, Distance, Time, Cost), reverseList(ReversedPath, Path).

%Appends the second list to the first list.
append([], L, L).
append([H | T], L2, [H | L]) :- append(T, L2, L).

%Prints [[car, a], [plane, b]] as "car to a \n plane to b".
print([]).
print([[Mode, Town] | Q]) :- write(Mode), write(' to '), write(Town), nl, print(Q).

%Prints a path, its distance, time and cost.
printTrip(Path, Distance, Time, Cost) :- print(Path), write('Total distance: '), write(Distance), write('km.'),
	nl, write('Total time: '), write(Time), write('h.'), nl, write('Total cost: $'), write(Cost), write('.').

%Finds a fastest path from town A to town B.
fastest(A, B, Path, Distance, Time, Cost) :- setof([P1, D1, T1, C1], findPath(A, B, P1, D1, T1, C1), Set),
	fastest(Set, [error, -1, 15, -1], [Path, Distance, Time, Cost]).
fastest([], [Path, Distance, Time, Cost], [Path, Distance, Time, Cost]) :- Time < 15.
fastest([[Path, Distance, Time, Cost] | Tail], [_, _, FastestTime, _], Fastest) :-
	Time < FastestTime, submember(b, Path), submember(e, Path), submember(c, Path), !, 
	fastest(Tail, [Path, Distance, Time, Cost], Fastest).
fastest([_ | Tail], FastestTime, Fastest) :- fastest(Tail, FastestTime, Fastest).

%Finds a fastest trip from A to A under 15h going to B, E and C
fastestTrip(A, Path, Distance, Time, Cost) :- fastest(A, A, Path, Distance, Time, Cost),
	printTrip(Path, Distance, Time, Cost).

%Finds a shortest path from town A to town B.
shortest(A, B, Path, Distance, Time, Cost) :- setof([P1, D1, T1, C1], findPath(A, B, P1, D1, T1, C1), Set),
	shortest(Set, [Path, Distance, Time, Cost]).
shortest([H | T], Shortest) :- shortest(T, H, Shortest).
shortest([], Shortest, Shortest).
shortest([[Path, Distance, Time, Cost] | Tail], [_, ShortestDistance, _, _], Shortest) :-
	Distance < ShortestDistance, !, shortest(Tail, [Path, Distance, Time, Cost], Shortest).
shortest([_ | Tail], ShortestDistance, Shortest) :- shortest(Tail, ShortestDistance, Shortest).

%Finds a shortest trip from A to B and back to A.
shortestTrip(A, B) :- shortest(A, B, P1, D1, T1, C1),
	shortest(B, A, [_ | P2], D2, T2, C2), append(P1, P2, Path), Distance is D1 + D2,
	Time is T1 + T2, Cost is C1 + C2, printTrip(Path, Distance, Time, Cost).

%Should generate all possibilities but does not (s a b c g f e d s) not present.
test(A, B) :- setof([P1, D1, T1, C1], findPath(A, B, P1, D1, T1, C1), Set), tPrint(Set).
tPrint([]).
tPrint([H | T]) :- write(H), nl, tPrint(T).