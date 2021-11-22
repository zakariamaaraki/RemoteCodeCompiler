// Radewoosh code
//~ while (clock()<=69*CLOCKS_PER_SEC)
//~ #pragma comment(linker, "/stack:200000000")
#pragma GCC optimize("O3")
//~ #pragma GCC target ("avx2")
//~ #pragma GCC optimize("Ofast")
//~ #pragma GCC target("sse,sse2,sse3,ssse3,sse4,popcnt,abm,mmx,avx,tune=native")
//~ #pragma GCC optimize("unroll-loops")
#include <bits/stdc++.h>
#include <ext/pb_ds/assoc_container.hpp>
#include <ext/pb_ds/tree_policy.hpp>

using namespace __gnu_pbds;
using namespace std;

template <typename T>
using ordered_set =
    tree<T, null_type, less<T>, rb_tree_tag, tree_order_statistics_node_update>;

#define sim template < class c
#define ris return * this
#define dor > debug & operator <<
#define eni(x) sim > typename \
  enable_if<sizeof dud<c>(0) x 1, debug&>::type operator<<(c i) {
sim > struct rge { c b, e; };
sim > rge<c> range(c i, c j) { return rge<c>{i, j}; }
sim > auto dud(c* x) -> decltype(cerr << *x, 0);
sim > char dud(...);
struct debug {
#ifdef LOCAL
~debug() { cerr << endl; }
eni(!=) cerr << boolalpha << i; ris; }
eni(==) ris << range(begin(i), end(i)); }
sim, class b dor(pair < b, c > d) {
  ris << "(" << d.first << ", " << d.second << ")";
}
sim dor(rge<c> d) {
  *this << "[";
  for (auto it = d.b; it != d.e; ++it)
    *this << ", " + 2 * (it == d.b) << *it;
  ris << "]";
}
#else
sim dor(const c&) { ris; }
#endif
};
#define imie(...) " [" << #__VA_ARGS__ ": " << (__VA_ARGS__) << "] "

#define shandom_ruffle random_shuffle

using ll=long long;
using pii=pair<int,int>;
using pll=pair<ll,ll>;
using vi=vector<int>;
using vll=vector<ll>;
const int nax=1200*1007;

int n, q, k;

ll kosz[nax];
ll miniko[nax];

pii zap[nax];
ll wyn[nax];

vi kto[nax];

vector<pll> stos;
vll sum;
vll sumli;

int main()
{
	scanf("%d%d%d", &n, &q, &k);
	for (int i=1; i<=n; i++)
		scanf("%lld", &kosz[i]);
	deque<int> kol;
	for (int i=n; i; i--)
	{
		while(!kol.empty() && kosz[i]<=kosz[kol.front()])
			kol.pop_front();
		kol.push_front(i);
		if (kol.back()==i+k)
			kol.pop_back();
		miniko[i]=kosz[kol.back()];
	}
	//~ debug() << range(miniko+1, miniko+1+n);
	for (int i=1; i<=q; i++)
	{
		int a, b;
		scanf("%d%d", &a, &b);
		kto[a].push_back(i);
		zap[i]={a, b};
	}
	for (int s=n; s>n-k; s--)
	{
		sum={0};
		sumli={0};
		stos.clear();
		for (int i=s; i>0; i-=k)
		{
			ll minuj=miniko[i];
			ll x=kosz[i];
			ll zebrane=0;
			while(!stos.empty() && minuj<stos.back().first)
			{
				zebrane+=stos.back().second;
				stos.pop_back();
				sum.pop_back();
				sumli.pop_back();
			}
			stos.push_back({minuj, zebrane});
			{
				ll w=sumli.back()+zebrane;
				sumli.push_back(w);
			}
			{
				ll w=sum.back()+zebrane*minuj;
				sum.push_back(w);
			}


			stos.push_back({x, 1});
			{
				ll w=sumli.back()+1;
				sumli.push_back(w);
			}
			{
				ll w=sum.back()+x;
				sum.push_back(w);
			}
			//~ debug() << i << " " << stos;

			for (int j : kto[i])
			{
				ll jest=sumli.back();
				ll chce=(zap[j].second-zap[j].first+k)/k;
				ll wsz=sum.back();
				chce=jest-chce;
				int bsa=0;
				int bsb=(int)stos.size();
				while(bsa<bsb)
				{
					int bss=(bsa+bsb+2)>>1;
					if (sumli[bss]<=chce)
						bsa=bss;
					else
						bsb=bss-1;
				}
				//~ debug() << j << " " << stos << " " << bsa;
				//~ debug() << sum << " " << sumli << " " << wsz << " " << chce;
				//~ debug();
				wsz-=sum[bsa];
				chce-=sumli[bsa];
				if (chce)
					wsz-=chce*stos[bsa].first;

				wyn[j]=wsz;
			}
		}
	}



	for (int i=1; i<=q; i++)
		printf("%lld\n", wyn[i]);
	return 0;
}