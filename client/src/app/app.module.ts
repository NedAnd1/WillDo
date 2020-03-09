import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { IonicModule } from '@ionic/angular';

import { AppComponent, DynamicView } from './app.component';
import { TaskDetailComponent } from './tasks/task-detail.component';
import { TaskListComponent } from './tasks/task-list.component';
import { GroupListComponent } from './groups/group-list.component';
import { GroupDetailComponent } from './groups/group-detail.component';

const appRoutes: Routes
	=
	[
		{
			path: 'group/:groupId',
			component: TaskListComponent,
			children: [
				{
					path: 'tasks',
					component: DynamicView.get(TaskDetailComponent) // wraps the component in a screen-dependent modal
				},
				{
					path: 'tasks/:taskId',
					component: DynamicView.get(TaskDetailComponent)
				}
			]
		},
		{
			path: '',
			component: TaskListComponent,
			children: [
				{
					path: 'tasks',
					component: DynamicView.get(TaskDetailComponent)
				},
				{
					path: 'tasks/:taskId',
					component: DynamicView.get(TaskDetailComponent)
				}
			]
		},
		{
			path: ':groupId',
			component: DynamicView.get(GroupDetailComponent),
			outlet: 'g'
		},
		{
			path: ':',
			pathMatch: 'full',
			component: DynamicView.get(GroupDetailComponent),
			outlet: 'g'
		}
	];


@NgModule({
  declarations: [
    AppComponent,
	TaskListComponent,
	GroupListComponent
  ].concat(
	DynamicView.getUserComponents(), // array of all components put in a dynamic context
	DynamicView.getGeneratedComponents() // array of all dynamically created components
  ),
  imports: [
    BrowserModule,
	FormsModule,
	HttpClientModule,
	IonicModule.forRoot(),
	RouterModule.forRoot(appRoutes,
		{
			onSameUrlNavigation: 'reload',
			anchorScrolling: 'enabled',
			paramsInheritanceStrategy: 'always'
		}
	)
  ],
  exports: [RouterModule],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: DynamicView.getUserComponents() // array of all components put a dynamic context
})
export class AppModule { }