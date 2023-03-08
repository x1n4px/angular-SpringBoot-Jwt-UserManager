import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangePassNoLoginComponent } from './change-pass-no-login.component';

describe('ChangePassNoLoginComponent', () => {
  let component: ChangePassNoLoginComponent;
  let fixture: ComponentFixture<ChangePassNoLoginComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangePassNoLoginComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangePassNoLoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
