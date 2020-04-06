import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';


const allusers = 'localhost:9090/allusers';
const routes: Routes = [];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
  getHeroes (): Observable<Hero[]> {
    return this.http.get<Hero[]>(this.allusers)
      .pipe(
        catchError(this.handleError<Hero[]>('getHeroes', []))
      );
  }
}
